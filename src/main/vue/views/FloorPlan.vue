<template>
  <q-page-container class="no-padding">
    <q-page>
      <div ref="mapContainer" class="floor-plan-editor-map">
        <div class="full-height" id="floor-plan-map"></div>
      </div>
    </q-page>
  </q-page-container>
</template>
<script>

import {CRS} from "leaflet";
import "@geoman-io/leaflet-geoman-free";
import "leaflet/dist/leaflet.css";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
import L from "leaflet";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {storeToRefs} from "pinia";
import {watch} from "vue";
import {getCssVar, useQuasar} from "quasar";
import CreateDoor from "@/main/vue/views/CreateDoor.vue";
import {useRoomStore} from "@/main/vue/stores/room";
import {useFloorStore} from "@/main/vue/stores/floor";
import {useLocationStore} from "@/main/vue/stores/locations";
import {useDoorStore} from "@/main/vue/stores/door";
import {useCredentialsStore} from "@/main/vue/stores/credential";
import {useI18n} from "vue-i18n";

const mapConfig = {
  crs: CRS.Simple,
  minZoom: 0,
  attributionControl: false,
  bounceAtZoomLimits: false,
  zoomControl: false
};

function getBounds(w, h) {
  let x;
  let y;

  if (w / 2 >= h) {
    x = 180;

    y = h * (x / w);
  } else if (h > w || w / 2 < h) {
    y = 90;

    x = w * (y / h);
  }

  let c1 = L.latLng(-y, -x);
  let c2 = L.latLng(y, x);

  return L.latLngBounds(c1, c2);
}

function getImageDimensions(imageURL) {
  const img = new Image();
  return new Promise((resolve, reject) => {
    img.onload = () => resolve({width: img.width, height: img.height});
    img.onerror = reject;
    img.src = imageURL;
  });
}

let floorPlanMap;
export default {
  name: "FloorPlan",
  props: {
    editView: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  setup() {
    const floorPlanStore = useFloorPlanStore();
    const floorStore = useFloorStore();
    const doorStore = useDoorStore();
    const locationStore = useLocationStore();
    const roomStore = useRoomStore();
    const credentialsStore = useCredentialsStore();
    const i18nLocale = useI18n()

    return {floorStore, floorPlanStore, locationStore, roomStore, doorStore, credentialsStore, i18nLocale}
  },
  mounted: function () {
    floorPlanMap = L.map("floor-plan-map", mapConfig);
    const {t} = useI18n()
    const $q = useQuasar();


    const {rooms} = storeToRefs(this.floorPlanStore)
    watch(rooms, () => {
      floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
      this.applyImageToMap(this.floorPlanStore.selectedFloorPlan)
      this.drawRooms(this.floorPlanStore.rooms)
    })

    floorPlanMap.pm.Draw.Polygon.setPathOptions({
      color: 'black',
      width: 1,
      fillOpacity: 0.1
    });
    floorPlanMap.pm.Draw.Line.setPathOptions({
      color: getCssVar('warning'),
      weight: 2
    });

    floorPlanMap.on('pm:drawstart', ({workingLayer}) => {
      workingLayer.on('pm:vertexadded', (e) => {
        if (e.shape === 'Line' && workingLayer.getLatLngs().length >= 2) {
          floorPlanMap.pm.Draw.Line._finishShape();
        }
      });

    });
    floorPlanMap.pm.Draw.Line.setOptions({
      hideMiddleMarkers: true
    })

    floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
    this.applyImageToMap(this.floorPlanStore.selectedFloorPlan);
    this.drawRooms(this.floorPlanStore.rooms)
    const {selectedRooms} = storeToRefs(this.floorPlanStore)
    watch(selectedRooms, () => {
      floorPlanMap.eachLayer(layer => {
        if (layer.type === "Room") {
          layer.setStyle({
            color: 'black',
            fillColor: 'black',
            weight: 1,
            fillOpacity: 0.1
          });
          selectedRooms.value.forEach(roomID => {
            if (roomID === layer.id) {
              layer.setStyle({
                color: 'red',
                fillColor: 'red',
                weight: 2,
                fillOpacity: 0.2
              });
            }
          })
        }
      })
    }, {deep: true})

    L.control.zoom({
      position: 'topright'
    }).addTo(floorPlanMap)

    const mapContainerObserver = new ResizeObserver(() => {
      floorPlanMap.invalidateSize();
    });

    floorPlanMap.on('pm:create', (e) => {
      if (e.layer instanceof L.Polygon || e.layer instanceof L.Rectangle) {
        e.layer.setStyle({
          color: 'black',
          width: 1,
          fillOpacity: 0.1
        });
      } else if (e.layer instanceof L.Polyline) {
        e.layer.setStyle({
          color: getCssVar('warning'),
          weight: 2
        });
      }

      if (e.shape === 'Rectangle' || e.shape === 'Polygon') {
        const floor = this.locationStore.getFloorById(this.floorPlanStore.selectedFloorId)
        const room = {
          name: 'New Room',
          coordinates: e.layer._latlngs[0].map((latLng) => ({
                lat: latLng.lat,
                lng: latLng.lng
              }
          )),
          doors: []
        }
        floor.rooms.push(room)

        this.floorStore.save(floor).then((savedFloor) => {
          const savedRoom = savedFloor.rooms.reduce((prev, current) => (prev.id > current.id) ? prev : current)
          this.floorPlanStore.rooms = savedFloor.rooms
          e.layer.id = savedRoom.id
          this.addCallbacksPolygon(e.layer)
        })
      } else if (e.shape === 'Line' || e.shape === 'Polyline') {
        $q.dialog({
          component: CreateDoor,
          componentProps: {
            rooms: this.floorPlanStore.rooms
          }
        }).onOk(({room, doorName, configuration}) => {
          let door = {
            name: doorName,
            doorConfigCmds: configuration,
            roomId: room.id,
            coordinates: e.layer._latlngs.map((latLng) => ({
                  lat: latLng.lat,
                  lng: latLng.lng
                }
            )),
          };
          const promise =this.doorStore.create(door)
          promise.then((savedDoor) => {
            room.doors.push(savedDoor)
            e.layer.id = savedDoor.id
            this.addCallbacksLine(e.layer)
            this.redrawRooms()
            this.$emit('doorCreated')
          })
          promise.catch(() => {
            $q.notify({
              type: 'negative',
              message: t('floorPlan.doorCreateFailed'),
              position: 'bottom',
              timeout: 3000,
            });
            floorPlanMap.removeLayer(e.layer)
          })
        }).onCancel(() => floorPlanMap.removeLayer(e.layer))
      }
    })


    mapContainerObserver.observe(this.$refs.mapContainer)
  },
  emits: [
      'doorCreated', 'roomClicked'
  ],
  methods: {
    redrawRooms() {
      this.removeLayer()
      this.drawRooms(this.floorPlanStore.rooms)
    },
    removeLayer() {
      floorPlanMap.eachLayer(layer => {
            if (layer.id) {
              floorPlanMap.removeLayer(layer)
            }
          }
      );
    },
    addEditControls(editView) {
      if (editView) {
        floorPlanMap.pm.addControls({
          position: 'topleft',
          drawCircle: false,
          drawCircleMarker: false,
          drawText: false,
          cutPolygon: false,
          rotateMode: false,
          drawMarker: false,
        });
      }

    },
    applyImageToMap(floorPlan) {
      getImageDimensions(floorPlan).then(({width, height}) => {

        const bounds = getBounds(width, height);
        floorPlanMap.setMaxBounds(bounds);
        floorPlanMap.fitBounds(bounds);

        let overlay = L.imageOverlay(floorPlan, bounds);
        overlay.addTo(floorPlanMap)

        let center = overlay.getCenter();
        floorPlanMap.panTo(center);
        this.addEditControls(this.editView)
      });
    },
    addCallbacksLine: function (line) {
      line.on('pm:update', (e) => {
        const door = this.locationStore.getDoorById(e.layer.id)
        door.coordinates = e.layer._latlngs.map((latLng) => ({
              lat: latLng.lat,
              lng: latLng.lng
            }
        ))
        this.doorStore.save(door)
      })

      line.on('pm:dragend', (e) => {
        const door = this.locationStore.getDoorById(e.layer.id)
        door.coordinates = e.layer._latlngs.map((latLng) => ({
              lat: latLng.lat,
              lng: latLng.lng
            }
        ))
        this.doorStore.save(door)
      });

      line.on('pm:remove', (e) => {
        const room = this.locationStore.getRoomById(e.layer.roomId)
        const index = room.doors.findIndex(door => e.layer.id === door.id)
        room.doors.splice(index, 1)
        this.roomStore.save(room)
      });

      line.pm._createMiddleMarker = () => {
      };
    },
    addCallbacksPolygon: function (polygon) {
      polygon.on('pm:update', (e) => {
        const room = this.locationStore.getRoomById(e.layer.id)
        room.coordinates = e.layer._latlngs[0].map((latLng) => ({
              lat: latLng.lat,
              lng: latLng.lng
            }
        ))
        this.roomStore.save(room)
      })
      polygon.on('pm:dragend', (e) => {
        const room = this.locationStore.getRoomById(e.layer.id)
        room.coordinates = e.layer._latlngs[0].map((latLng) => ({
              lat: latLng.lat,
              lng: latLng.lng
            }
        ))
        this.roomStore.save(room)
      });

      polygon.on('pm:remove', (e) => {
        const floor = this.locationStore.getFloorById(this.floorPlanStore.selectedFloorId)
        const index = floor.rooms.findIndex(room => e.layer.id === room.id)
        floor.rooms.splice(index, 1)
        this.floorStore.save(floor).then((savedFloor) => {
          this.floorPlanStore.rooms = savedFloor.rooms
        })
        floorPlanMap.eachLayer(layer => {
          if (layer.roomId === e.layer.id) {
            floorPlanMap.removeLayer(layer)
          }
        })
      });
      polygon.on('click', (e) => {
        console.log(polygon.id)
        this.$emit('roomClicked', polygon.id)
      })
    }, async drawRooms(rooms) {
      let polygons = []
      let polylines = []
      await this.credentialsStore.fetch()
      for (const room of rooms) {
        const polygon = L.polygon(room.coordinates?.map(coord => L.latLng(coord.lat, coord.lng)), {
          color: 'black',
          weight: 1,
          fillOpacity: 0.1
        })
        if (this.floorPlanStore.selectedRooms.includes(room.id)) {
          polygon.setStyle({
            color: 'red',
            fillColor: 'red',
            weight: 2,
            fillOpacity: 0.1
          })
        }

        polygons.push(polygon);

        polygon.on('click', function () {
          for (const p of polygons) {
            p.setStyle({
              color: 'black',
              fillColor: 'black',
              weight: 1,
              fillOpacity: 0.1
            });
          }

          polygon.setStyle({
            color: 'red',
            fillColor: 'red',
            weight: 2,
            fillOpacity: 0.1
          })
        });

        polygon.id = room.id
        polygon.type = "Room"
        let roomPopup = "<b>" + room.name + "</b><br>";

        for (const door of room.doors) {
          const line = L.polyline(door.coordinates?.map(coord => L.latLng(coord.lat, coord.lng)), {
            color: getCssVar('warning'),
            weight: 2
          })
          line.id = door.id
          line.roomId = room.id




          let activeConfig = this.getActiveBaseConf(door);

          let timeString = activeConfig ? (activeConfig.baseConfig ? ' (Basiskonf.) ' : " (" + activeConfig.startTime + " - " + activeConfig.endTime + ")") : ''
          let configurationString = "<b>Konfiguration für " + door.name + timeString + ":</b>" ;

          if (activeConfig) {
            configurationString += "<br>"
            if (JSON.stringify(activeConfig.proofConfigIn) === JSON.stringify(activeConfig.proofConfigOut)) {
              configurationString += "<div style='margin-left: 1em'>"
              let activeCredentials = this.getConfigString(activeConfig.proofConfigIn);
              activeCredentials = [...new Set(activeCredentials)]
              configurationString += activeCredentials.join("<br> <b>UND</b> <br>")
              configurationString += "</div>"
            } else {
              configurationString += "<b>Rein:</b><br><div style='margin-left: 1em'>"
              let activeCredentialsIn = this.getConfigString(activeConfig.proofConfigIn);
              activeCredentialsIn = [...new Set(activeCredentialsIn)]
              configurationString += activeCredentialsIn.join("<br> <b>UND</b> <br>")
              configurationString += "</div>"

              configurationString += "<b>Raus:</b><br><div style='margin-left: 1em'>"
              let activeCredentialsOut = this.getConfigString(activeConfig.proofConfigOut);
              activeCredentialsOut = [...new Set(activeCredentialsOut)]
              configurationString += activeCredentialsOut.join("<br> <b>UND</b> <br>")
              configurationString += "</div>"
            }
          } else {
            configurationString += ` <i>${this.i18nLocale.t('editor.config.noConfig')}</i><br>`
          }
          configurationString += "</div>"
          line.bindTooltip(configurationString);
          this.addCallbacksLine(line);
          roomPopup += configurationString
          polylines.push(line)
        }
        polygon.bindTooltip(roomPopup);
        polygon.addTo(floorPlanMap);
        this.addCallbacksPolygon(polygon);
      }
      for (const door of polylines) {
        door.addTo(floorPlanMap)
      }

    },
    getConfigString(config) {
      const activeCredentials = []
      for (const attribute in config.requestedAttributes) {
        activeCredentials.push(config
            .requestedAttributes[attribute]
            .restrictions
            .filter(rest => rest.credentialDefinitionId)
            .map(rest => this.credentialsStore.getByDefinitionId(rest.credentialDefinitionId)?.name || "")
            .join(" <b>ODER</b> "))
      }
      for (const attribute in config.requestedPredicates) {
        activeCredentials.push(config
            .requestedPredicates[attribute]
            .restrictions
            .filter(rest => rest.credentialDefinitionId)
            .map(rest => this.credentialsStore.getByDefinitionId(rest.credentialDefinitionId)?.name || "")
            .join(" <b>ODER</b> "))
      }
      return activeCredentials
    },
    getActiveBaseConf(door) {
      if (door.doorConfigs.length > 1) {
        const currentDate = new Date();

        for (const doorConfig of door.doorConfigs) {
          if (!doorConfig?.baseConfig) {

            const startTime = new Date();
            startTime.setHours(doorConfig?.startTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            const endTime = new Date();
            endTime.setHours(doorConfig?.endTime?.split(":")[0], doorConfig?.startTime?.split(":")[1])
            if (startTime < currentDate && endTime > currentDate) {
              return doorConfig
            }
          }
        }

        for (const doorConfig of door.doorConfigs) {
          if (doorConfig.baseConfig) {
            return doorConfig;
          }
        }
      } else {
        return door.doorConfigs[0]
      }

    }
  },
};
</script>
<style>
.floor-plan-editor-map {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
}

#floor-plan-map {
  position: relative;
  background-color: var(--bg-color);
}
</style>