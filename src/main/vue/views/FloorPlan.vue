<template>
  <q-page-container style="padding: 2em">
    <q-page>
      <q-btn v-if="false" dense round unelevated class="bg-black"
             style="z-index: 1000; top: 15px; left: -17px"></q-btn>
      <div ref="mapContainer" class="floor-plan-editor-map">
        <div id="floor-plan-map"></div>
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
import {useQuasar} from "quasar";
import SelectRoom from "@/main/vue/views/SelectRoom.vue";
import {useRoomStore} from "@/main/vue/stores/room";
import {useFloorStore} from "@/main/vue/stores/floor";
import {useLocationStore} from "@/main/vue/stores/locations";
import {useDoorStore} from "@/main/vue/stores/door";

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
    return {floorStore, floorPlanStore, locationStore, roomStore, doorStore}
  },
  mounted: function () {
    floorPlanMap = L.map("floor-plan-map", mapConfig);

    const $q = useQuasar();

    const {rooms} = storeToRefs(this.floorPlanStore)
    watch(rooms, () => {
      floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
      this.applyImageToMap(this.floorPlanStore.selectedFloorPlan)
      this.drawRooms(this.floorPlanStore.rooms)
    })

    floorPlanMap.pm.Draw.Polygon.setPathOptions({
      color: 'black',
      width: 5,
      fillOpacity: 0.1
    });
    floorPlanMap.pm.Draw.Line.setPathOptions({
      color: '#b0b0b0',
      weight: 3
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
            weight: 2,
            fillOpacity: 0.1
          });
          selectedRooms.value.forEach(room => {
            if (room.id === layer.id) {
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
          width: 5,
          fillOpacity: 0.1
        });
      } else if (e.layer instanceof L.Polyline) {
        e.layer.setStyle({
          color: '#b0b0b0',
          weight: 3
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
          e.layer.id = savedRoom.id
          this.addCallbacksPolygon(e.layer)
        })
      } else if (e.shape === 'Line' || e.shape === 'Polyline') {
        $q.dialog({
          component: SelectRoom,
          componentProps: {
            rooms: this.floorPlanStore.rooms
          }
        }).onOk((room) => {
          room.doors.push({
            name: 'door',
            coordinates: e.layer._latlngs.map((latLng) => ({
                  lat: latLng.lat,
                  lng: latLng.lng
                }
            )),
          })
          this.roomStore.save(room).then((savedRoom) => {
            const savedDoor = savedRoom.doors.reduce((prev, current) => (prev.id > current.id) ? prev : current)
            e.layer.id = savedDoor.id
            this.addCallbacksLine(e.layer)
          })
        })
      }
    })


    mapContainerObserver.observe(this.$refs.mapContainer)
  },
  methods: {
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
        this.floorStore.save(floor)
        floorPlanMap.eachLayer(layer => {
          if (layer.roomId === e.layer.id) {
            floorPlanMap.removeLayer(layer)
          }
        })
      });
    }, drawRooms(rooms) {
          let polygons = [];
          for (const room of rooms) {
              const polygon = L.polygon(room.coordinates?.map(coord => L.latLng(coord.lat, coord.lng)), {
                  color: 'black',
                  width: 5,
                  fillOpacity: 0.1
              })

              polygons.push(polygon);
              let doorsname = room.doors.map(door => door.name).join(", ");
              let doorscredentials = room.doors.flatMap(door => door.credentials).map(credential => credential.name).join(", ");
              let issuer = room.doors.flatMap(door => door.credentials).flatMap(cred => cred.issuer).map(issuer => issuer.firstname +" " + issuer.lastname).join(", ");
              console.log(room.doors);
              const popup = L.popup();
              let string = "Raumnummer: " + room.id.toString() + "<br>Türen: " + doorsname + "<br>Credentials: " + doorscredentials + "<br>Issuer: " + issuer;
              let url = `<a href="/credentialview?q=${room.id}"> Mehr Informationen zu Credentials</a>`;

              popup.setContent(url);
              polygon.bindTooltip(string).openTooltip();
              polygon.bindPopup(popup);
              polygon.addTo(floorPlanMap);

              polygon.on('click', function() {
                  for (const p of polygons) {
                      p.setStyle({
                          color: 'black',
                          fillColor: 'black',
                          weight: 5,
                          fillOpacity: 0.1
                      });
                  }

                  polygon.setStyle({
                      color: 'red',
                      fillColor: 'red',
                      weight: 2,
                      fillOpacity: 0.1
                  })});

        polygon.id = room.id
        polygon.type = "Room"
        for (const door of room.doors) {
          const line = L.polyline(door.coordinates?.map(coord => L.latLng(coord.lat, coord.lng)), {
            color: '#b0b0b0',
            weight: 3
          }).addTo(floorPlanMap)
          line.id = door.id
          line.roomId = room.id
          this.addCallbacksLine(line);
        }
        this.addCallbacksPolygon(polygon);
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
  height: 100%;
  position: relative;
}
</style>