<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('doorHistory.title')}}</p>
    <div class="row self-center" style="display: flex">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :separator="'cell'"
          :rows="doors"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :columns="columns"
          row-key="id"
          :filter="filter"
      >
        <template v-slot:body-cell-credentials="props">
          <q-td :props="props">
            <div v-for="credential in props.row.credentials" :key="credential.id">{{ credential.name }}</div>
          </q-td>
        </template>
        <template v-slot:top-right>
          <q-input class="q-ml-xs" borderless dense debounce="250" v-model="filter" :placeholder="t('common.search')">
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" icon="visibility" @click="openDialog(props.row); prompt = true" />
            <q-btn dense round flat color="grey" icon="content_copy" />
          </q-td>
        </template>
      </q-table>
    </div>
    <q-dialog v-model="prompt" persistent>
      <q-card style="width: 90%">
        <div class="row justify-center" style="padding: 2em 2em" >
          <q-input  filled v-model="doorName" label="Door" disable />
          <q-input class="q-ml-md" filled v-model="roomName" label="Room" disable />
        </div>
        <door-config disabled ref="configIn" :door-config="doorConfigIn"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                     @changeDirection="changeDirectionOut"></door-config>
        <door-config disabled v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                     :door-config="doorConfigOut" :is-config-out="true"></door-config>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat v-close-popup> {{ t("common.cancel")}}</q-btn>
          <p v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)">
          <q-btn flat v-close-popup> {{ t("common.save")}}</q-btn>
          </p>
        </q-card-actions>

      </q-card>
    </q-dialog>
  </q-page>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import {useI18n} from "vue-i18n";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import CreateDoor from "@/main/vue/views/CreateDoor.vue";
import { Door } from '../entity/location';
import {Direction} from "@/main/vue/entity/doorConfiguration";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {prop} from "vue-class-component";
import {useUserStore} from "@/main/vue/stores/users";



export default {
  computed: {
    Direction() {
      return Direction
    }
  },
  components: {CreateDoor, DoorConfig},
  props: {
    doorConfigIn: {
      required: false
    },
    doorConfigOut: {
      required: false
    }
  },
  emits: [
    'ok'
  ],
  setup() {
    const { t } = useI18n();
    const selectedDoor = ref<Door | null>(null);
    const doors = ref<Door[]>([]);
    const dialogVisible = ref(false);
    const userStore = useUserStore()
    const doorName = ref('');

    const columns = [
      { name: 'name', required: true, label: t('doorHistory.name'), align: 'center', field: 'name', headerAlign: 'center', sortable: true },
      { name: 'credentials', required: true, label: t('doorHistory.credentials'), align: 'center', field: 'credentials', headerAlign: 'center', sortable: true },
      { name: 'actions', align: 'center', label: t('doorHistory.edit') },
    ]

    const fetchDoors = async (): Promise<void> => {
      try {
        const response = await axios.get<Door[]>('/api/door/doors');
        doors.value = response.data;
      } catch (error) {
        console.error('Error fetching doors:', error);
      }
    }

    const showHistory = (door: Door): void => {
      selectedDoor.value = door;
    }

    const openDialog = (row: Door): void => {
      doorName.value = row.name;
      dialogVisible.value = true;
    }

    const closeDialog = (): void => {
      dialogVisible.value = false;
    }

    onMounted(() => {
      fetchDoors();
    })

    return {
      t,
      selectedDoor,
      columns,
      doors,
      dialogVisible,
      fetchDoors,
      showHistory,
      openDialog,
      closeDialog,
      roomName: ref(''),
      prompt: ref(false),
      filter: ref(''),
      userStore,
      doorName,
    };
  },

}
</script>


<style scoped>
.center-cell {
  text-align: center;
}
</style>
