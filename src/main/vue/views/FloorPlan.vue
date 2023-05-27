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
import { storeToRefs } from "pinia";
import {watch} from "vue";
import credential from "@/main/vue/api/credential";

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
  mounted: function () {
    floorPlanMap = L.map("floor-plan-map", mapConfig);
    const floorPlanStore = useFloorPlanStore();
    const { rooms } = storeToRefs(floorPlanStore)
    watch(rooms, () => {
      floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
      this.applyImageToMap(floorPlanStore.selectedFloorPlan)
      this.drawRooms(floorPlanStore.rooms)
    })
    floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
    this.applyImageToMap(floorPlanStore.selectedFloorPlan);
    this.drawRooms(floorPlanStore.rooms)
    const { selectedRooms } = storeToRefs(floorPlanStore)
    watch(selectedRooms, () => {
      floorPlanMap.eachLayer(layer => {
        if(layer.type === "Room") {
          layer.setStyle({
            color: 'black',
            fillColor: 'black',
            weight: 2,
            fillOpacity: 0.1
          });
          selectedRooms.value.forEach(room => {
            if(room.id === layer.id) {
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
    mapContainerObserver.observe(this.$refs.mapContainer)
  },
  methods: {
    applyImageToMap(floorPlan) {
      getImageDimensions(floorPlan).then(({width, height}) => {

        const bounds = getBounds(width, height);
        floorPlanMap.setMaxBounds(bounds);
        floorPlanMap.fitBounds(bounds);

        let overlay = L.imageOverlay(floorPlan, bounds);
        overlay.addTo(floorPlanMap)

        let center = overlay.getCenter();
        floorPlanMap.panTo(center);
      });
    },
      drawRooms(rooms) {
          let polygons = [];

          for (const room of rooms) {
              const polygon = L.polygon(room.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {
                  color: 'black',
                  width: 5,
                  fillOpacity: 0.1
              });

              polygons.push(polygon);
              let doorsname = room.doors.map(door => door.name).join(", ");
              let doorscredentials = room.doors.flatMap(door => door.credentials).map(credential => credential.name).join(", ");
              let issuer = room.doors.flatMap(door => door.credentials).flatMap(cred => cred.issuer).map(issuer => issuer.firstname +" " + issuer.lastname).join(", ");
              console.log(room.doors);
              const popup = L.popup();
              let string = "Raumnummer: " + room.id.toString() + "<br>Türen: " + doorsname + "<br>Credentials: " + doorscredentials + "<br>Issuer: " + issuer;
              let url = "<a href='./credentialview'> Mehr Informationen zu Credentials</a>";
              popup.setContent(url);
              polygon.bindTooltip(string).openTooltip();
              polygon.bindPopup(popup);
              polygon.addTo(floorPlanMap);

              for (const door of room.doors) {
                  L.polyline(door.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {
                      color: '#b0b0b0',
                      weight: 3
                  }).addTo(floorPlanMap);
              }

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
                  });
              });
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