<template>
  <q-drawer
      show-if-above bordered
      v-model="show"
      content-class="bg-grey-1"
      style="background-color: var(--bg-color); color: var(--text-color)">
    <q-list>
      <q-item-label
          header
          class="text-grey-8">
        {{ t('floorPlan.locations') }}
      </q-item-label>
      <q-expansion-item
          v-for="(location,i) in locationStore.allLocations"
          :header-inset-level="0"
          :default-opened="i===0"
          :default-closed="i!==1"
          class="show-location">
        <template v-slot:header>
          <q-item-section>
            {{ location.name }}
          </q-item-section>
          <q-item-section side>
            <q-btn round @click.stop icon="more_vert" flat class="edit-menu-location">
              <q-menu>
                <q-list style="min-width: 100px">
                  <q-item clickable @click.stop="editLocation(location)" v-close-popup>
                    <q-item-section>{{t('floorPlan.editLocation')}}</q-item-section>
                  </q-item>
                  <q-item clickable @click.stop="addBuilding(location)" v-close-popup>
                    <q-item-section>{{t('floorPlan.addBuilding')}}</q-item-section>
                  </q-item>
                  <q-item clickable v-close-popup @click.stop="deleteLocation = true; setParameter(location)">
                    <q-item-section>{{t('floorPlan.deleteLocation')}}</q-item-section>
                  </q-item>
                </q-list>
              </q-menu>
            </q-btn>
          </q-item-section>
        </template>
        <q-expansion-item
            v-for="(building,i) in location.buildings"
            :label="building.name"
            :header-inset-level="1"
            :default-opened="i===0"
            :default-closed="i!==1"
            default-closed
            class="show-building"
        >

          <template v-slot:header>
            <q-item-section>
              {{ building.name }}
            </q-item-section>
            <q-item-section side>
              <q-btn round @click.stop icon="more_vert" flat class="edit-menu-building">
                <q-menu class="show-building">
                  <q-list style="min-width: 100px">
                    <q-item clickable @click.stop="editBuilding(building)" v-close-popup>
                      <q-item-section>{{t('floorPlan.editBuilding')}}</q-item-section>
                    </q-item>
                    <q-item clickable @click.stop="addFloor(building)" v-close-popup>
                      <q-item-section>{{t('floorPlan.addFloor')}}</q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click.stop="deleteBuilding = true; setParameter(building)">
                      <q-item-section>{{t('floorPlan.deleteBuilding')}}</q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
            </q-item-section>
          </template>
          <q-item clickable v-ripple v-for="(floor) in building.floors" :inset-level="2"
                  @click="changeFloorPlan(floor)" :active="floorPlanStore.selectedFloorId === floor.id" class="show">
            <q-item-section>{{ floor.floorLevel === 0 ? "Erdgeschoss" : "Etage " + floor.floorLevel }}</q-item-section>

            <q-item-section side>
              <q-btn round icon="more_vert" @click.stop flat class="edit-menu">
                <q-menu>
                  <q-list style="min-width: 100px">
                    <q-item clickable @click.stop="editFloor(floor)" v-close-popup>
                      <q-item-section>{{t('floorPlan.editFloor')}}</q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click.stop="deleteFloor = true; setParameter(floor)">
                      <q-item-section>{{t('floorPlan.deleteFloor')}}</q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-btn>
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </q-expansion-item>
      <q-item>
        <q-btn color="primary" icon="add" :label="t('floorPlan.addLocation')" @click="addLocation" flat/>
      </q-item>
    </q-list>
    <div class="q-mini-drawer-hide absolute" style="top: 15px; right: -17px">
      <q-btn
          dense
          round
          unelevated
          color="primary"
          icon="chevron_left"
          @click="show = false"
      />
    </div>
  </q-drawer>
  <q-btn
      v-if="show === false"
      style="width: 4.8em; height: 2.4em; padding-right: 0; z-index: 1000; margin-top: 15px; margin-left: -2.4em; border-radius: 2.4em"
      @click="show = true"
      class="absolute"
      color="primary"
      direction="right"
  ><q-icon name="chevron_right" right/></q-btn>
  <q-dialog v-model="deleteLocation" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{t('floorPlan.deleteLocation')}}</div>
      </q-card-section>
      <q-card-section class="row items-center">
        <span class="q-mx-sm">{{t('floorPlan.deleteLocationQuestion')}}</span>
      </q-card-section>

      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup>  {{ t("common.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="deleteLocationFunction(param)">  {{ t("common.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="deleteBuilding" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{t('floorPlan.deleteBuilding')}}</div>
      </q-card-section>
      <q-card-section class="row items-center">
        <span class="q-mx-sm">{{t('floorPlan.deleteBuildingQuestion')}}</span>
      </q-card-section>

      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup>  {{ t("common.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="deleteBuildingFunction(param)">  {{ t("common.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="deleteFloor" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6"> {{t('floorPlan.deleteFloor')}}</div>
      </q-card-section>
      <q-card-section class="row items-center">
        <span class="q-mx-sm">{{t('floorPlan.deleteFloorQuestion')}}</span>
      </q-card-section>

      <q-card-actions align="right" class="text-primary">
        <q-btn flat v-close-popup>  {{ t("common.cancel")}}</q-btn>
        <q-btn flat v-close-popup @click="deleteFloorFunction(param)">  {{ t("common.save")}} </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-page class="row no-wrap">
    <FloorPlan @roomClicked="openDetail" @door-created="refresh" ref="floorPlanRef" :edit-view="true" class="full-width"></FloorPlan>
    <FloorPlanRoomList ref="roomList" @doorChanged="redrawRooms" @editRoom="redrawRooms" :edit="true"></FloorPlanRoomList>
  </q-page>

</template>

<script>

import FloorPlan from "@/main/vue/views/FloorPlan.vue";
import {useLocationStore} from "@/main/vue/stores/locations";
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useFloorStore} from "@/main/vue/stores/floor";
import {useQuasar} from "quasar";
import EditLocation from "@/main/vue/views/EditLocation.vue";
import EditBuilding from "@/main/vue/views/EditBuilding.vue";
import EditFloor from "@/main/vue/views/EditFloor.vue";
import FloorPlanRoomList from "@/main/vue/views/FloorPlanRoomList.vue";
import {useBuildingStore} from "@/main/vue/stores/buildings";


export default {
  name: 'FloorPlanEdit',
  components: {FloorPlanRoomList, FloorPlan},
  methods: {
    redrawRooms() {
      this.$refs.floorPlanRef.removeLayer()
      this.$refs.floorPlanRef.drawRooms(this.floorPlanStore.rooms)
    },
    refresh() {
      this.$refs?.roomList?.refreshDetail();
    },
    openDetail(id) {
      this.$refs?.roomList?.roomClick(this.locationStore.getRoomById(id))
    }
  },
  setup() {
    const locationStore = useLocationStore()
    const floorPlanStore = useFloorPlanStore()
    const buildingStore = useBuildingStore()
    const floorStore = useFloorStore();
    let show = ref(true)
    const $q = useQuasar()
    const {t} = useI18n()
    const param = ref()

    function setParameter(parameter) {
     param.value = parameter.id
    }

    function deleteLocationFunction(id) {
      locationStore.deleteLocation(id).then(() => floorPlanStore.selectedFloorPlan = null)
    }

    function deleteBuildingFunction(id) {
      buildingStore.deleteBuilding(id).then(() => floorPlanStore.selectedFloorPlan = null)
    }

    function deleteFloorFunction(id) {
      floorStore.deleteFloor(id).then(() => floorPlanStore.selectedFloorPlan = null)
    }

    const changeFloorPlan = function (floor) {
      floorPlanStore.selectedFloorId = floor.id
      floorPlanStore.selectedFloorPlan = floor.floorPlanPath
      floorPlanStore.rooms = floor.rooms
    }
    const editLocation = function (location) {
      $q.dialog({
        component: EditLocation,
        componentProps: {
          location: location
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: t('floorPlan.locationEditSuccess'),
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addLocation = function () {
      $q.dialog({
        component: EditLocation,
        componentProps: {
          location: {}
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: t('floorPlan.locationSaveSuccess'),
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const editBuilding = function (building) {
      $q.dialog({
        component: EditBuilding,
        componentProps: {
          building: building
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: t('floorPlan.buildingEditSuccess'),
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addBuilding = function (location) {
      $q.dialog({
        component: EditBuilding,
        componentProps: {
          building: {}
        }
      }).onOk((building) => {
        location.buildings.push(building)
        locationStore.save(location).then(() => {
          $q.notify({
            type: 'positive',
            message: t('floorPlan.buildingSaveSuccess'),
            position: 'bottom',
            timeout: 3000,
          });
        })
      })
    }

    const editFloor = function (floor) {
      $q.dialog({
        component: EditFloor,
        componentProps: {
          floor: floor
        }
      }).onOk(() => {
        $q.notify({
          type: 'positive',
          message: t('floorPlan.floorEditSuccess'),
          position: 'bottom',
          timeout: 3000,
        });
      })
    }

    const addFloor = function (building) {
      $q.dialog({
        component: EditFloor,
        componentProps: {
          floor: {}
        }
      }).onOk((floor) => {
        building.floors.push(floor)
        buildingStore.save(building).then(_ => {
          $q.notify({
            type: 'positive',
            message: t('floorPlan.floorSaveSuccess'),
            position: 'bottom',
            timeout: 3000,
          });
        })
      })
    }

    locationStore.getLocations().then((res) => {
      changeFloorPlan(res[0].buildings[0].floors[0])
    })


    return {
      show,
      t,
      param,
      locationStore,
      changeFloorPlan,
      editLocation,
      editBuilding,
      editFloor,
      deleteLocationFunction,
      deleteBuildingFunction,
      deleteFloorFunction,
      floorPlanStore,
      addFloor,
      addBuilding,
      addLocation,
      setParameter,
      deleteLocation: ref(false),
      deleteBuilding: ref(false),
      deleteFloor: ref(false),
    }
  }
}
</script>
<style>
.show .edit-menu {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show:hover .edit-menu {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}

.show-building .edit-menu-building {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show-building:hover .edit-menu-building {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}

.show-location .edit-menu-location {
  visibility: hidden;
  opacity: 0;
  transition: .3s;
}

.show-location:hover .edit-menu-location {
  visibility: visible;
  opacity: 1;
  transition: .3s;
}
</style>