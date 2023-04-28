<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Impressum</h1>

      <div class="impressum-content" v-html="impressumContent"></div>

      <router-link to="/" class="back-to-home">Homepage</router-link>
    </div>
  </q-page>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: "Impressum",
  setup() {
    const impressumContent = ref('');

    const fetchImpressumContent = async () => {
      try {
        const response = await axios.get('/api/impressum');
        impressumContent.value = response.data;
        setDecodedImageSrc();
      } catch (error) {
        console.error('Error fetching impressum content:', error);
      }
    };

    const setDecodedImageSrc = () => {
      const imgElem = document.querySelector('.impressum-content img');
      if (imgElem) {
        const encodedSrc = imgElem.getAttribute('data-src');
        if (encodedSrc !== null) {
          const decodedSrc = decodeURIComponent(encodedSrc);
          imgElem.setAttribute('src', decodedSrc);
        }
      }
    };


    onMounted(fetchImpressumContent);

    return {
      impressumContent,
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

.back-to-home {
  display: inline-block;
  margin-top: 1em;
  text-decoration: underline;
  cursor: pointer;
}
</style>