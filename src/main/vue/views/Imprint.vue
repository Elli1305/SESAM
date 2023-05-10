<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Impressum</h1>

      <div class="imprint-content" v-html="imprintContent"></div>

    </div>
  </q-page>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: "Imprint",
  setup() {
    const imprintContent = ref('');

    const fetchImprintContent = async () => {
      try {
        const response = await axios.get('/api/imprint');
        imprintContent.value = response.data;
        setDecodedImageSrc();
      } catch (error) {
        console.error('Error fetching impressum content:', error);
      }
    };

    const setDecodedImageSrc = () => {
      const imgElem = document.querySelector('.imprint-content img');
      if (imgElem) {
        const encodedSrc = imgElem.getAttribute('data-src');
        if (encodedSrc !== null) {
          const decodedSrc = decodeURIComponent(encodedSrc);
          imgElem.setAttribute('src', decodedSrc);
        }
      }
    };


    onMounted(fetchImprintContent);

    return {
      imprintContent,
    };
  },

}
</script>


<style scoped>
h1 {
  font-size: 3em;
  text-align: center;
  margin-bottom: -0.5em;
}


</style>