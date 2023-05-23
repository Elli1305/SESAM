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
    floorPlanStore.$subscribe((mutation, state) => {
      floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
      this.applyImageToMap(state.selectedFloorPlan)
      this.drawRooms(state.rooms)
    });
    floorPlanMap.eachLayer(layer => floorPlanMap.removeLayer(layer));
    this.applyImageToMap(floorPlanStore.selectedFloorPlan);
    this.drawRooms(floorPlanStore.rooms)

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
      for (const room of rooms) {
        const polygon = L.polygon(room.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {
          color: 'black',
          width: 5,
          fillOpacity: 0.1
        })

        polygon.on('click', (layer) => layer.setColor);
        const popup = L.popup();

        polygon.bindPopup(popup)

        polygon.addTo(floorPlanMap)
        for (const door of room.doors) {
          L.polyline(door.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {
            color: '#b0b0b0',
            weight: 3
          }).addTo(floorPlanMap)
        }
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