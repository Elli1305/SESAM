<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('doorHistory.title')}}</p>
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
            <div>{{ formatDateTime(props.row.createdAt) }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-credentials="props">
          <q-td :props="props">
            <div v-for="credential in props.row.credentials" :key="credential.id">{{ credential.name }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" icon="visibility" @click="openDialog(props.row)" />
            <q-btn dense round flat color="grey" icon="content_copy" />
          </q-td>
        </template>
      </q-table>
    </div>

    <q-dialog v-model="dialogVisible" persistent>
      <q-card>
        <q-card-section>
          <q-card-title>
            :label="t('doorHistory.dialogTitle')"
          </q-card-title>
          <div v-if="selectedDoor">

          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat color="primary" label="Close" @click="closeDialog" />
        </q-card-actions>
      </q-card>
    </q-dialog>  </q-page>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import {useI18n} from "vue-i18n";

interface Door {
  id: number;
  name: string;
  createdAt: number;
  coordinates: Coordinate[];
  credentials: Credential[];
}

interface Coordinate {
  id: number;
  lng: number;
  lat: number;
}

interface Credential {
  id: number;
  name: string;
  credentialDefinitionId: string;
  form: FormField[];
  checklist: ChecklistItem[];
  issuer: Issuer[];
}

interface FormField {
  id: number;
  label: string;
  type: string;
  attributeName: string;
}

interface ChecklistItem {
  id: number;
  label: string;
}

interface Issuer {
  id: number;
  firstName: string;
  lastName: string;
  roles: Role[];
  room: Room;
  credentials: Credential[];
  enabled: boolean;
  username: string;
  accountNonExpired: boolean;
  credentialsNonExpired: boolean;
  authorities: string[];
  accountNonLocked: boolean;
}

interface Role {
  role: string;
  granted: boolean;
}

interface Room {
  id: number;
  name: string;
  doors: Door[];
  coordinates: Coordinate[];
}

export default defineComponent({
  setup() {
    const { t } = useI18n();
    const selectedDoor = ref<Door | null>(null);
    const doors = ref<Door[]>([]);
    const dialogVisible = ref(false);

    const columns = [
      { name: 'datum', required: true, label: t('doorHistory.date'), align: 'center', field: 'createdAt', headerAlign: 'center', sortable: true },
      { name: 'name', required: true, label: t('doorHistory.name'), align: 'center', field: 'name', headerAlign: 'center', sortable: true },
      { name: 'credentials', required: true, label: t('doorHistory.credentials'), align: 'center', field: 'credentials', headerAlign: 'center', sortable: true },
      { name: 'actions', align: 'center', label: t('doorHistory.edit') },
    ];

    const fetchDoors = async (): Promise<void> => {
      try {
        const response = await axios.get<Door[]>('/api/door/doors');
        doors.value = response.data;
      } catch (error) {
        console.error('Error fetching doors:', error);
      }
    };

    const showHistory = (door: Door): void => {
      selectedDoor.value = door;
    };

    const formatDateTime = (dateTime: number): string => {
      const options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric' };
      return new Date(dateTime).toLocaleDateString('en-US', options);
    };

    const openDialog = (row: Door): void => {
      selectedDoor.value = row;
      dialogVisible.value = true;
    };

    const closeDialog = (): void => {
      dialogVisible.value = false;
    };

    onMounted(() => {
      fetchDoors();
    });

    return {
      t,
      selectedDoor,
      columns,
      doors,
      dialogVisible,
      fetchDoors,
      showHistory,
      formatDateTime,
      openDialog,
      closeDialog,
    };
  },
});
</script>


<style scoped>
.center-cell {
  text-align: center;
}
</style>
