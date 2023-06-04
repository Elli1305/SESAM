<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t('imprint.imprintTitle') }}</h1>

      <div class="imprint-content" v-html="imprintContent"></div>

    </div>
  </q-page>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {useI18n} from "vue-i18n";


export default {

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
    const {t} = useI18n();

    onMounted(fetchImprintContent);

    return {
      imprintContent,
      t,
    };
  },
};
</script>

<style scoped>
h1 {
  font-size: 3em;
  text-align: center;
  margin-bottom: -0.5em;
}
</style>
