<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('doorHistory.title')}}</p>
    <div class="row self-center" style="display: flex">
      <q-table
          style="width: 80vw; height: 50vh"
          :rows-per-page-options="[0]"
          :separator="'cell'"
          title="Türhistorie"
          :rows="doors"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :columns="columns"
          row-key="id"
          :filter="filter"
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
        <q-input v-model="doorName" label="Door" disable />
        <q-input v-model="roomName" label="Room" disable />
        <door-config disabled ref="configIn" :door-config="doorConfigIn"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.IN : Direction.BOTH"
                     @changeDirection="changeDirectionOut"></door-config>
        <door-config disabled v-show="$refs.configIn?.direction !== Direction.BOTH" ref="configOut"
                     :direction="JSON.stringify(this.doorConfigIn) !== JSON.stringify(this.doorConfigOut) ? Direction.OUT : Direction.BOTH"
                     :door-config="doorConfigOut" :is-config-out="true"></door-config>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat v-close-popup> {{ t("common.cancel")}}</q-btn>
          <q-btn flat v-close-popup> {{ t("common.save")}}</q-btn>
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

    const columns = [
      { name: 'datum', required: true, label: t('doorHistory.date'), align: 'center', field: 'createdAt', headerAlign: 'center', sortable: true },
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

    const formatDateTime = (dateTime: number): string => {
      const options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric' };
      return new Date(dateTime).toLocaleDateString('en-US', options);
    }

    const openDialog = (row: Door): void => {
      selectedDoor.value = row;
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
      formatDateTime,
      openDialog,
      closeDialog,
      doorName: ref(''),
      roomName: ref(''),
      prompt: ref(false),
      filter: ref('')
    };
  },
  methods: {

    changeDirectionOut(direction: Direction) {
      if (direction === Direction.IN) {
        this.$refs.configOut.direction = Direction.OUT
      } else if (direction === Direction.OUT) {
        this.$refs.configOut.direction = Direction.IN
      }
    },
  }
}
</script>


<style scoped>
.center-cell {
  text-align: center;
}
</style>
