<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t("home.issuerPages")}}</p>
    <div class="row self-center">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          title="Predefined Configurations"
          :separator="'horizontal'"
          hide-header
          hide-bottom
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :filter="filter"
          row-key="name">
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn flat rounded color="primary" label="Ausstellen" :to="'/credentials/' + props.row.id + '/issue'"/>
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import {Ref, ref} from "vue";
import {useI18n} from "vue-i18n";
import {Credential} from "@/main/vue/entity/credentialDefinition";
import api from "@/main/vue/api";


const filter = ref('')
const { t } = useI18n()
const columns = [
  {
    name: 'name',
    required: true,
    align: 'center',
    field: 'name',
    sortable: true
  },
  { name: 'actions', align: 'center'}
]

const rows: Ref<Credential[]> = ref([])
credentialStore.getCredentialsByIssuer(userStore.user?.id).then((response) => {
  rows.value = response
})


api.credential.all().then((r) => credentials.value = r.data);
</script>

<style scoped>
</style>