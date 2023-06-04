<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Impressum</p>
    <div class="row self-center justify-center" style="width: 80vw; height: 25em">
      <p style="font-size: 1.25em" v-html="imprintContent"/>
    </div>
  </q-page>
</template>

<script>
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
      } catch (error) {
        console.error('Error fetching imprint content:', error);
      }
    };

    onMounted(fetchImprintContent);

    return {
      imprintContent,
    };
  },
};
</script>

<style scoped>
</style>
