<template>
  <q-page>
    <q-btn v-if="false" dense round unelevated class="bg-black" style="z-index: 1000; top: 15px; left: -17px"></q-btn>
    <div ref="mapContainer" class="site-plan-editor-map">
      <div id="site-plan-map"></div>
    </div>
  </q-page>
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

let sitePlanMap;
export default {
  name: "FloorPlan",
  mounted: function () {
    sitePlanMap = L.map("site-plan-map", mapConfig);
    const floorPlanStore = useFloorPlanStore();
    floorPlanStore.$subscribe((mutation, state) => {
      sitePlanMap.eachLayer(layer => sitePlanMap.removeLayer(layer));
      this.applyImageToMap(state.selectedFloorPlan)
      this.drawRooms(state.rooms)
    });
    sitePlanMap.eachLayer(layer => sitePlanMap.removeLayer(layer));
    this.applyImageToMap(floorPlanStore.selectedFloorPlan);
    this.drawRooms(floorPlanStore.rooms)

    L.control.zoom({
      position: 'topright'
    }).addTo(sitePlanMap)

    const mapContainerObserver = new ResizeObserver(() => {
      sitePlanMap.invalidateSize();
    });
    mapContainerObserver.observe(this.$refs.mapContainer)
  },
  methods: {
    applyImageToMap(floorPlan) {
      getImageDimensions(floorPlan).then(({width, height}) => {

        const bounds = getBounds(width, height);
        sitePlanMap.setMaxBounds(bounds);
        sitePlanMap.fitBounds(bounds);

        let overlay = L.imageOverlay(floorPlan, bounds);
        overlay.addTo(sitePlanMap)

        let center = overlay.getCenter();
        sitePlanMap.panTo(center);
      });
    },
    drawRooms(rooms) {
      for (const room of rooms) {
        L.polygon(room.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {color: 'black', width: 5, fillOpacity: 0.1}).addTo(sitePlanMap)
        for (const door of room.doors) {
          L.polyline(door.coordinates.map(coord => L.latLng(coord.lat, coord.lng)), {color: '#b0b0b0', weight: 3}).addTo(sitePlanMap)
        }
      }
    }
  },
};
</script>
<style>
.site-plan-editor-map {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
}
#site-plan-map {
  height: 100%;
  position: relative;
}
</style>