<template>
    <q-page-container style="padding: 2em">
        <q-page>
            <div ref="mapContainer" class="site-plan-editor-map">
                <div id="site-plan-map"></div>
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
  bounceAtZoomLimits: false
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
      this.applyImageToMap(state.selectedFloorPlan)
      this.drawRooms(state.rooms)
    });
    this.applyImageToMap(floorPlanStore.selectedFloorPlan);

    const mapContainerObserver = new ResizeObserver(() => {
          sitePlanMap.invalidateSize();
    });
    mapContainerObserver.observe(this.$refs.mapContainer)
    this.drawRooms(floorPlanStore.rooms)

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
        sitePlanMap.invalidateSize();
      });
    },
    drawRooms(rooms) {
      for (const room of rooms) {
        L.polygon(room.coordinates.map(coord => L.latLng(coord.lat, coord.lng))).addTo(sitePlanMap)
      }
    }
  },
};
</script>
<style>
#site-plan-map {
  width: 100%;
  height: 85.8vh;
}
</style>