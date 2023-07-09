<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">Tür History</p>
    <div class="row self-center" style="display: flex">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :separator="'cell'"
          :rows="doors"
          :columns="columns"
          row-key="id"
          :expanded.sync="expanded"
          @row-click="showHistory"
      >
        <template v-slot:body-cell-datum="props">
          <q-td :props="props" class="center-cell">
            <div>{{ formatDateTime(props.value) }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-credentials="props">
          <q-td :props="props">
            <div v-for="credential in props.row.credentials">{{ credential.name }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" icon="visibility"  @click="seeInfo = true; openForm(props.row)" />
            <q-btn dense round flat color="grey" icon="content_copy" />
          </q-td>
        </template>
        <q-dialog v-model="seeInfo" persistent>
          <q-card>
            <q-card-section>
              <q-card-title>
                Credential Information
              </q-card-title>

              <div v-for="field in selectedCredential.form" :key="field.id">
                <div>{{ field.label }}:</div>
                <div>{{ getFieldData(field) }}</div>
              </div>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn flat color="primary" label="Close" @click="closeDialog" />
            </q-card-actions>
          </q-card>
        </q-dialog>
      </q-table>
    </div>
  </q-page>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios';

interface Door {
  id: number;
  name: string;
  createdAt: number;
  configurations?: Configuration[];
  credentials: Credential[];
}

interface Configuration {
  id: number;
  description: string;
  createdAt: number;
}

interface Credential {
  id: number;
  name: string;
}

export default defineComponent({
  data() {
    return {
      selectedDoor: null as Door | null,
      columns: [
        { name: 'datum', required: true, label: 'Datum', align: 'center', field: 'createdAt', headerAlign: 'center',  sortable: true },
        { name: 'name', required: true, label: 'Tür', align: 'center', field: 'name', headerAlign: 'center',  sortable: true },
        { name: 'credentials', required: true, label: 'Credentials', align: 'center', field: 'credentials', headerAlign: 'center',  sortable: true },
        { name: 'actions', align: 'center', label: 'Action',}
      ],
      doors: [] as Door[],
    };
  },
  methods: {
    async fetchDoors(): Promise<void> {
      try {
        const response = await axios.get<Door[]>('/api/door/doors');
        this.doors = response.data;
      } catch (error) {
        console.error('Error fetching doors:', error);
      }
    },
    showHistory(door: Door): void {
      this.selectedDoor = door;
    },
    formatDateTime(dateTime: number): string {
      const options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric' };
      return new Date(dateTime).toLocaleDateString('en-US', options);
    },
  },
  mounted() {
    this.fetchDoors();
  },
});
</script>

<style scoped>
.center-cell {
  text-align: center;
}
</style>
