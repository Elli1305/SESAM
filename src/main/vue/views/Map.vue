<template>
  <q-page>

  <div class="site-plan-editor-map">
    <div
        id="site-plan-map"
        :style="{ width: '100%', height: '100%'}"
    ></div>
  </div>
  </q-page>
</template>
<script>

import {map} from "leaflet/src/map";
import {LatLng, LatLngBounds} from "leaflet/src/geo";
import {CRS} from "leaflet/dist/leaflet-src.esm";
import {imageOverlay, rectangle, tileLayer} from "leaflet/src/layer";
import {control} from "leaflet/src/control";

const mapConfig = {
  crs: CRS.Simple,
  minZoom: 0,
  attributionControl: false,
  bounceAtZoomLimits: false
};

function getBounds(w, h) {

  let c1 = new LatLng(-h, -w);
  let c2 = new LatLng(h, w);

  return new LatLngBounds(c1, c2);
}

function getImageDimensions(imageURL) {
  const img = new Image();
  return new Promise((resolve, reject) => {
    img.onload = () => resolve({ width: img.width, height: img.height });
    img.onerror = reject;
    img.src = imageURL;
  });
}

export default {


  data: function () {
    return {
      imageURL: "src/main/resources/citec-gebaeudeplan.png",
    };
  },
  mounted: function () {
    this.init();
  },
  methods: {
    async init() {
      await this.initMap();
    },
    async initMap() {
      await this.applyImageToMap();
    },

    async applyImageToMap() {
      let sitePlanMap = map("site-plan-map", mapConfig);
      const {width, height} = await getImageDimensions("src/main/resources/citec-gebaeudeplan.png");
      const bounds = getBounds(width, height);
      sitePlanMap.setMaxBounds(bounds);
      sitePlanMap.fitBounds(bounds);

      let overlay = imageOverlay("src/main/resources/citec-gebaeudeplan.png", bounds);

      overlay.addTo(sitePlanMap)

      let center = overlay.getCenter();
      control.zoom().addTo(sitePlanMap)
      sitePlanMap.panTo(center);
      sitePlanMap.invalidateSize();
    },
  },
};
</script>
<style>

</style>