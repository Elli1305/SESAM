<template>
  <q-page-container class="no-padding no-margin">
    <q-page style="padding-right: 1em; padding-top: 2em">

      <div v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)">


        <q-tabs
            v-model="tab"
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify">
          <q-tab name="rooms" :label="t('floorPlan.rooms')"/>
          <q-tab name="groups" :label="t('editor.groupRooms.groups')"/>
          <div class="row justify-center" style="width: 3em">
            <q-icon size="1.25em" fixed-right color="white" name="info_outlined" class="q-pl-xs">
              <q-tooltip class="grey" anchor="bottom right" max-width="200px" self="top middle"
                         style="font-size: 1em"
                         :offset="[0, 0]">
                {{ t('editor.groupRooms.info') }}
              </q-tooltip>
            </q-icon>
          </div>

        </q-tabs>
        <q-separator></q-separator>

        <q-tab-panels v-model="tab" animated style="max-width: 24em; min-width: 24em; background-color: var(--bg-color); color: var(--text-color)">
          <q-tab-panel name="rooms">

            <q-input
                :placeholder="t('common.search')"
                v-model="search"
                @update:model-value="roomFilter"
                clearable
                outlined
                rounded
                clear-icon="clear"
                style="margin-bottom: 1em; min-width: 20em">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
            <q-scroll-area style="height: 24em; width: 22em; overflow: hidden"
                           :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
            >
              <q-list>
                <q-item v-for="room in filteredRooms" style="padding-left: 0">
                  <q-checkbox v-model="selectedRooms" :val="room.id"
                              color="blue"/>
                  <q-btn-dropdown
                      split
                      flat
                      style="min-width: 18em"
                      :label="room.name"
                      dropdown-icon="expand_more"
                      color="var(--text-color)"
                      @click="toggleRoomCheckbox(room);">
                    <q-list>
                      <q-item-label>{{ t("floorPlan.roomName") }}:</q-item-label>
                      <q-item-label>{{ t("floorPlan.doors") }}:</q-item-label>
                      <q-item-label>Credentials:</q-item-label>
                    </q-list>
                    <q-list>
                      <q-item-label>{{ room.name }}</q-item-label>
                      <q-item-label>{{
                          room.doors.map(door => door.name).join(", ")
                        }}
                      </q-item-label>
                      <q-item-label>
                        {{ room?.id ? "U-MEMBER" : "" }}
                      </q-item-label>
                    </q-list>
                    <div v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
                      <q-separator></q-separator>
                      <div class="row justify-center" style="padding: 0.5em">
                        <p class="cursor-pointer q-mb-none"
                           :style="{color: getCssVar('primary')}"
                           @click="setOldValueR(room)">{{ t('common.edit') }}</p>
                        <q-dialog v-model="inception">
                          <q-card>
                            <q-card-section>
                              <div class="text-h6">{{ t("floorPlan.editRoom") }}</div>
                              <div class="q-mt-md">
                                <q-input filled v-model="currentRoomName"
                                         :label="t( 'floorPlan.roomName')"
                                         stack-label
                                         style="width: 250px; padding-bottom: 1em"/>
                              </div>
                              <div class="q-my-xs">
                                <q-list bordered class="rounded-borders"
                                        style="max-width: 600px">
                                  <q-item-label header>
                                    <div class="row items-center">
                                      <div class="q-mr-sm">{{
                                          t("floorPlan.doors")
                                        }}
                                      </div>

                                    </div>
                                  </q-item-label>
                                  <template v-for="door in room.doors">
                                    <q-item class="q-mb-sm">
                                      <q-item-section avatar top>
                                        <q-icon name="meeting_room"
                                                color="black" size="34px"/>
                                      </q-item-section>

                                      <q-item-section>
                                        <q-item-label lines="1">
                                                                                    <span class="text-weight-medium">{{
                                                                                        door.name
                                                                                      }}</span>
                                        </q-item-label>

                                      </q-item-section>

                                      <q-item-section top side>
                                        <div class="text-grey-8 q-gutter-xs">
                                          <q-btn class="gt-xs" size="12px"
                                                 flat dense round
                                                 icon="delete"
                                                 @click="deleteDoorDialog= true"/>
                                          <q-btn size="12px" flat dense
                                                 round
                                                 icon="edit"
                                                 @click="openDialog(door)"/>
                                        </div>
                                      </q-item-section>
                                    </q-item>
                                    <div class="row justify-end">

                                      <q-dialog v-model="deleteDoorDialog"
                                                persistent
                                                transition-show="scale"
                                                transition-hide="scale">
                                        <q-card>
                                          <q-card-section class="q-pa-md">
                                            <div class="text-h6">{{
                                                t("floorPlan.confirmDeletion")
                                              }}
                                            </div>
                                            <div class="q-mt-md">{{
                                                t("floorPlan.confirmDeletionText")
                                              }}
                                            </div>
                                          </q-card-section>

                                          <q-card-actions align="right"
                                                          class="bg-white text-teal">
                                            <q-btn flat color="primary"
                                                   :label="t( 'common.cancel')"
                                                   v-close-popup/>
                                            <q-btn flat color="primary"
                                                   :label="t('common.delete')"
                                                   @click="deleteDoor(room, door)"
                                                   v-close-popup/>
                                          </q-card-actions>
                                        </q-card>
                                      </q-dialog>

                                    </div>
                                  </template>
                                </q-list>
                              </div>
                              <q-card-actions align="right" class="text-primary">
                                <q-btn flat :label="t( 'common.cancel')"
                                       color="primary" v-close-popup/>
                                <q-btn flat :label="t( 'common.save')"
                                       color="primary" @click="save(room)"
                                       v-close-popup/>
                              </q-card-actions>
                            </q-card-section>
                          </q-card>
                        </q-dialog>
                      </div>
                    </div>
                  </q-btn-dropdown>
                </q-item>
              </q-list>
            </q-scroll-area>
            <div v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
              <q-separator></q-separator>
              <div class="column q-pa-xs">
                <q-btn class="row no-wrap q-my-xs" style="min-width: 10em" color="primary"
                       @click="checkAddRoomsToNewGroup();" rounded flat>
                  <q-icon name="add" class="q-mr-xs"/>
                  <span>{{t('editor.groupRooms.addRoomsToNewGroup')}}</span>
                </q-btn>
                <q-btn class="row no-wrap q-my-xs" style="min-width: 10em" color="primary"
                       @click="addRoomsToExistingGroupDialog=checkIfGroupSelected();" rounded flat>
                  <q-icon name="add" class="q-mr-xs"/>
                  <span>{{t('editor.groupRooms.addRooms')}}</span>
                </q-btn>
              </div>


            </div>

          </q-tab-panel>
          <q-tab-panel name="groups">
            <q-input
                :placeholder="t('common.search')"
                v-model="searchGroup"
                @update:model-value="groupFilter"
                clearable
                outlined
                rounded
                clear-icon="clear"
                style="margin-bottom: 1em; min-width: 20em">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
            <q-scroll-area style="height: 28em; width: 22em; overflow: hidden" @scroll="giveLog();dropdown = false"
                           :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
            >
              <q-list>

                <q-item v-for="group in allGroups" style="padding-left: 0">
                  <q-radio @click="filterRoomToGroups(); updateNumRoomsInGroup();"
                           v-model="selectedGroups" :val="group"
                           color="blue"/>
                  <q-btn-dropdown
                      flat
                      style="min-width: 16em; max-width: 16em"
                      :label="group.name"
                      dropdown-icon="none"
                      class="q-pl-xl"
                      color="var(--text-color)"
                      @click="selectGroup(group); filterRoomToGroups(); updateNumRoomsInGroup();">
                  </q-btn-dropdown>
                  <q-btn-dropdown

                      flat
                      menu-anchor="bottom right"
                      style="max-width: 0"
                      dropdown-icon="expand_more"
                      color="var(--text-color)"
                      @click="selectGroup(group); filterRoomToGroups(); updateNumRoomsInGroup();">

                    <div class="column no-wrap" style="background-color: var(--bg-color); color: var(--text-color)">
                      <div class="row no-wrap" style="min-width: 18em">
                        <div class="column no-wrap q-pl-lg" style="padding: 0.5em">
                          <q-list>
                            <q-item-label>{{ t("floorPlan.roomAmount") }}:</q-item-label>
                          </q-list>
                        </div>
                        <div class="column no-wrap" style="padding: 0.5em">
                          <q-list>
                            <q-item-label>{{ numRoomsInGroup }}</q-item-label>

                          </q-list>
                        </div>
                      </div>
                      <div
                          v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
                        <q-separator></q-separator>

                        <div class="row justify-center" style="padding: 0.5em">
                          <p class="cursor-pointer q-mb-none"
                             :style="{color: getCssVar('primary')}"
                             @click="reloadRoomsBE(); setOldValueG(group); allFloorsForGroup()">
                            {{ t('common.edit') }}</p>
                          <q-dialog v-model="editGroupD" persistent>
                            <q-card>
                              <q-card-section>
                                <div class="text-h6">{{
                                    t("floorPlan.editGroup")
                                  }}
                                </div>
                                <div class="q-mt-md">
                                  <q-input filled v-model="currentGroupName"
                                           :label="t( 'floorPlan.groupName')"
                                           stack-label
                                           style="width: 100%; padding-bottom: 1em"/>
                                </div>
                                <div class="q-my-xs">
                                  <q-scroll-area
                                      style="height: 25em; max-width: 3000px;">
                                    <q-list bordered class="rounded-borders"
                                            style="max-width: 600px">
                                      <q-item-label header>
                                        <div class="row items-center">
                                          <div class="q-mr-sm">{{
                                              t("floorPlan.rooms")
                                            }}
                                          </div>

                                        </div>
                                      </q-item-label>
                                      <template v-for="(room, i) in group.rooms"
                                                class="getFloorToRoom(room);">

                                        <q-item class="q-mb-sm">

                                          <q-item-section>
                                            <div class="col">
                                              <div class="row">
                                                <div class="col">
                                                  <q-item-label lines="1" class="row">
                                                    <span style="padding-right: 10em" class="text-weight-medium">{{room.name }}</span>
                                                  </q-item-label>
                                                  <q-item-label caption class="row">
                                                    {{ t('editor.groupRooms.floor') }}
                                                    {{ arrayFloors[i] }}
                                                  </q-item-label>
                                                </div>
                                                <q-btn
                                                    dense
                                                    style="padding-left: 1em;"
                                                    flat
                                                    icon="delete"
                                                    class="col"
                                                    color="grey"
                                                    :label="t('common.delete')"
                                                    @click="addToDeleteList(room);"/>
                                              </div>
                                            </div>




                                          </q-item-section>

                                          <q-item-section top side>
                                            <div class="text-grey-8 q-gutter-xs">

                                            </div>
                                          </q-item-section>
                                        </q-item>
                                        <div class="row justify-end">

                                        </div>
                                      </template>
                                    </q-list>
                                  </q-scroll-area>
                                </div>


                                <q-card-actions align="right" class="text-primary">
                                  <q-btn style="max-width: 10em;"
                                         class="q-mr-xl text-white bg-primary"
                                         :label="t('editor.groupRooms.doorconfiguration')"
                                         @click="prompt=true; getRoomsAndDoors()"
                                  />

                                  <q-btn flat :label="t( 'common.cancel')"
                                         @click="unCheck();cancelEdit();"
                                         v-close-popup/>
                                  <q-btn flat :label="t( 'common.save')"
                                         color="primary"
                                         @click="editGroupName(); updateNumRoomsInGroup();" v-close-popup/>
                                </q-card-actions>
                              </q-card-section>
                            </q-card>
                          </q-dialog>
                        </div>
                      </div>

                    </div>
                  </q-btn-dropdown>
                </q-item>
              </q-list>
            </q-scroll-area>
            <div
                v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
              <q-separator></q-separator>
              <div class="q-pa-xs q-pt-sm">
                <q-btn
                    color="primary"
                    icon="add"
                    :label="t('editor.groupRooms.newGroup')"
                    @click="delName(); newGroup = true;"
                    rounded flat/>
                <q-btn
                    color="grey"
                    icon="delete"
                    :label="t('common.delete')"
                    @click="deleteAlert = checkGroupSelected();"
                    rounded flat/>
              </div>


            </div>

          </q-tab-panel>
        </q-tab-panels>
      </div>


      <div v-else class="full-height">


        <q-input
            :placeholder="t('common.search')"
            v-model="search"
            @update:model-value="roomFilter"
            clearable
            outlined
            rounded
            clear-icon="clear"
            style="margin-bottom: 1em; min-width: 20em">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
        <div>
          <q-scroll-area style="min-width: 21em; height: 35em; max-width: 22em;"
                         :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
          >
            <q-list>
              <q-item v-for="room in filteredRooms" style="padding-left: 0">
                <q-checkbox v-model="selectedRooms" :val="room.id" color="blue"/>
                <q-btn-dropdown
                    split
                    flat
                    style="min-width: 16em"
                    :label="room.name"
                    dropdown-icon="expand_more"
                    color="var(--text-color)"
                    @click="toggleRoomCheckbox(room)">
                  <div class="column no-wrap" style="background-color: var(--bg-color); color: var(--text-color)">
                    <div class="row no-wrap">
                      <div class="column no-wrap" style="padding: 0.5em">
                        <q-list>
                          <q-item-label>{{ t("floorPlan.roomName") }}:</q-item-label>
                          <q-item-label>{{ t("floorPlan.doors") }}:</q-item-label>
                          <q-item-label>Credentials:</q-item-label>
                        </q-list>
                      </div>
                      <div class="column no-wrap" style="padding: 0.5em">
                        <q-list>
                          <q-item-label>{{ room.name }}</q-item-label>
                          <q-item-label>{{
                              room.doors.map(door => door.name).join(", ")
                            }}
                          </q-item-label>
                          <q-item-label>
                            {{ room?.id ? "U-MEMBER" : "" }}
                          </q-item-label>
                        </q-list>
                      </div>
                    </div>
                    <div
                        v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted) && edit">
                      <q-separator></q-separator>
                      <div class="row justify-center" style="padding: 0.5em">
                        <p class="cursor-pointer q-mb-none"
                           :style="{color: getCssVar('primary')}"
                           @click="setOldValueR(room)">{{ t('common.edit') }}</p>
                        <q-dialog v-model="inception">
                          <q-card>
                            <q-card-section>
                              <div class="text-h6">{{ t("floorPlan.editRoom") }}</div>
                              <div class="q-mt-md">
                                <q-input filled v-model="currentRoomName"
                                         :label="t( 'floorPlan.roomName')" stack-label
                                         style="width: 250px; padding-bottom: 1em"/>
                              </div>
                              <div class="q-my-xs">
                                <q-list bordered class="rounded-borders"
                                        style="max-width: 600px">
                                  <q-item-label header>
                                    <div class="row items-center">
                                      <div class="q-mr-sm">{{
                                          t("floorPlan.doors")
                                        }}
                                      </div>

                                    </div>
                                  </q-item-label>
                                  <template v-for="door in room.doors">
                                    <q-item class="q-mb-sm">
                                      <q-item-section avatar top>
                                        <q-icon name="meeting_room"
                                                color="black"
                                                size="34px"/>
                                      </q-item-section>

                                      <q-item-section>
                                        <q-item-label lines="1">
                                                                            <span class="text-weight-medium">{{
                                                                                door.name
                                                                              }}</span>
                                        </q-item-label>

                                      </q-item-section>

                                      <q-item-section top side>
                                        <div class="text-grey-8 q-gutter-xs">
                                          <q-btn class="gt-xs" size="12px"
                                                 flat dense
                                                 round icon="delete"
                                                 @click="deleteDoorDialog= true"/>
                                          <q-btn size="12px" flat dense round
                                                 icon="edit"
                                                 @click="openDialog(door)"/>
                                        </div>
                                      </q-item-section>
                                    </q-item>
                                    <div class="row justify-end">

                                      <q-dialog v-model="deleteDoorDialog"
                                                persistent
                                                transition-show="scale"
                                                transition-hide="scale">
                                        <q-card>
                                          <q-card-section class="q-pa-md">
                                            <div class="text-h6">
                                              {{
                                                t("floorPlan.confirmDeletion")
                                              }}
                                            </div>
                                            <div class="q-mt-md">{{
                                                t("floorPlan.confirmDeletionText")
                                              }}
                                            </div>
                                          </q-card-section>

                                          <q-card-actions align="right"
                                                          class="bg-white text-teal">
                                            <q-btn flat color="primary"
                                                   :label="t( 'common.cancel')"
                                                   v-close-popup/>
                                            <q-btn flat color="primary"
                                                   :label="t('common.delete')"
                                                   @click="deleteDoor(room, door)"
                                                   v-close-popup/>
                                          </q-card-actions>
                                        </q-card>
                                      </q-dialog>

                                    </div>
                                  </template>
                                </q-list>
                              </div>
                              <q-card-actions align="right" class="text-primary">
                                <q-btn flat :label="t( 'common.cancel')"
                                       color="primary"
                                       v-close-popup/>
                                <q-btn flat :label="t( 'common.save')"
                                       color="primary"
                                       @click="save(room)" v-close-popup/>
                              </q-card-actions>
                            </q-card-section>
                          </q-card>
                        </q-dialog>
                      </div>
                    </div>
                  </div>
                </q-btn-dropdown>
              </q-item>
            </q-list>
          </q-scroll-area>
        </div>


      </div>

      <q-dialog v-model="newGroup">
        <q-card>
          <q-card-section>
            <div class="text-h6">{{t('editor.groupRooms.nameOfGroup')}} </div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-input dense v-model="newGroupName" autofocus @keyup.enter="prompt = false"/>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="makeANewGroup(newGroupName);"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
      <q-dialog v-model="addRoomsToExistingGroupDialog">
        <q-card>
          <q-card-section>
            <div class="text-h6">{{t('editor.groupRooms.addRoomsToSelected')}} {{ selectedGroups.name }} ?</div>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="addRoomsToGroups();" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>

      <q-dialog v-model="addRoomsToNewGroupDialog">
        <q-card>
          <q-card-section>
            <div class="text-h6">Name der Gruppe</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-input dense v-model="newGroupName" autofocus @keyup.enter="prompt = false"/>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat :label="t('common.cancel')" @click="" v-close-popup/>
            <q-btn flat :label="t('common.save')" @click="addRoomsToNewGroup();"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </q-page>
    <q-dialog v-model="deleteAlert">
      <q-card>
        <q-card-section>
          <div class="text-h6">{{ t('admin.currentUsers.editUser.deleteUser') }}</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          {{ t('editor.groupRooms.question') }}
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat :label="t('common.cancel')" v-close-popup/>
          <q-btn flat :label="t('common.delete')" @click="deleteGroup(); deleteAlert=false"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="prompt" persistent>
      <q-card style="min-width: 50em; background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{t("editor.groupRooms.groupsConfig")}}</div>
        </q-card-section>
        <q-card-section>
          <q-table
              style="background-color: var(--bg-color); color: var(--text-color)"
              flat bordered
              hide-bottom
              :rows-per-page-options="[0]"
              :title="t('editor.groupRooms.roomSelection')"
              :rows="rows2"
              :columns="columns2"
              :filter="searchinput"
              row-key="name"
              separator="cell"
              :no-data-label="t('common.noData')"
              :no-results-label="t('common.noResults')"
              :pagination-label="getPaginationLabel"
              :visible-columns="visibleColumns"
          >
            <template v-slot:top-right>
              <div v-if="$q.screen.gt.xs" class="col" style="padding-right: 2em">
                <q-toggle v-model="visibleColumns" @update:model-value="fetchDoors(rows2)" val="doorNames" :label="t('editor.groupRooms.doors')" size="2.5em"/>
              </div>
              <q-input class="q-ml-xs" outlined rounded dense debounce="250" v-model="searchinput" :placeholder="t('common.search')">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
            <template v-slot:body-cell-doorNames="props">
              <q-td style="width: 60%" :props="props">
                <q-select v-if="props.row.selected"
                          class="q-my-sm"
                          filled dense options-dense
                          emit-value
                          v-model="model[props.row.room]"
                          multiple
                          :options="props.row.doors"
                          option-label="name"
                          options-cover
                          map-options
                          use-chips
                />
              </q-td>
            </template>
            <template v-slot:body-cell-actions="props">
              <q-td style="width: 10%" :props="props">
                <q-checkbox v-model="props.row.selected" @update:model-value="getDoors(props.row.room); checkIfSelected(props.row.selected, props.row.room)"/>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
        <q-card-section>
          <q-select
              class="q-ml-md"
              style="min-width: 20em"
              filled
              use-input
              hide-selected
              fill-input
              input-debounce="0"
              @filter="filterConfigOptions"
              :label="t('floorPlan.chooseConfig')"
              option-label="name"
              v-model="selectedConfig"
              :options="configOptions"
              clearable
          >
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </q-card-section>
        <q-card-section v-for="(selectConf,k) in qSelectGeneral.qSelectsSet">
          <q-card bordered flat>
            <q-toolbar class="bg-primary text-accent">

              <q-toolbar-title>Konfiguration</q-toolbar-title>
              <q-icon class="q-mr-xs" color="accent" size="1.25em" name="info_outlined">
                <q-tooltip max-width="15em" anchor="center right" self="center left">
                  You can only choose one base configuration.
                </q-tooltip>
              </q-icon>
              <q-td v-if=!(checkLength())>
                <q-btn flat round icon="delete" size="0.75em" @click="removeConfig(k)"/>
              </q-td>
            </q-toolbar>


            <div class="colomn q-mt-sm justify-around items-center no-wrap">

              <div class="q-pa-md">
                <div class="q-gutter-sm">

                  <q-checkbox dense v-model="qSelectGeneral.qSelectsSet[k].baseConfig" label="Basis Konfiguration"
                              color="primary" @click="check(k)"/>

                </div>
              </div>
              <q-td class="q-pl-md v-if=" v-if="!qSelectGeneral.qSelectsSet[k].baseConfig">
                <div class="q-gutter-sm row">
                  <div class="q-gutter-sm row">
                    <q-input filled v-model="qSelectGeneral.qSelectsSet[k].startTime" mask="time" :rules="['time']">
                      <template v-slot:append>
                        <q-icon name="access_time" class="cursor-pointer">
                          <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                            <q-time v-model="qSelectGeneral.qSelectsSet[k].startTime">
                              <div class="row items-center justify-end">
                                <q-btn v-close-popup label="Close" color="primary" flat/>
                              </div>
                            </q-time>
                          </q-popup-proxy>
                        </q-icon>
                      </template>
                    </q-input>
                  </div>
                  <div class="q-gutter-sm row">
                    <q-input filled v-model="qSelectGeneral.qSelectsSet[k].endTime" mask="time" :rules="['time']"
                             :disabled="qSelectGeneral.qSelectsSet[k].baseConfig">
                      <template v-slot:append>
                        <q-icon name="access_time" class="cursor-pointer">
                          <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                            <q-time v-model="qSelectGeneral.qSelectsSet[k].endTime">
                              <div class="row items-center justify-end">
                                <q-btn v-close-popup label="Close" color="primary" flat/>
                              </div>
                            </q-time>
                          </q-popup-proxy>
                        </q-icon>
                      </template>
                    </q-input>
                  </div>
                </div>
              </q-td>
            </div>
            <door-config :door-config="qSelectGeneral.qSelectsSet[k].doorConfigIn"
                         :direction="JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigIn) !==
                        JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigOut) ? Direction.IN : Direction.BOTH"
                         @changeDirection="changeDirectionOut($event, k)" ref="doorIn">
            </door-config>
            <door-config v-show="JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigIn.direction) !==
                         JSON.stringify(qSelectGeneral.qSelectsSet[k].doorConfigOut.direction)"
                         :direction="Direction.BOTH"
                         :door-config="qSelectGeneral.qSelectsSet[k].doorConfigOut" :is-config-out="true"
                         ref="doorOut">
            </door-config>

            <q-btn class="q-ml-sm q-mb-sm" flat dense rounded color="primary" icon="add" @click="addConfiguration">
              Konfiguration hinzufügen
            </q-btn>
          </q-card>
        </q-card-section>
        <q-card>
          <q-card-actions align="right" class="text-primary">
            <q-btn flat @click="resetConfig()" v-close-popup> {{ t("common.cancel") }}</q-btn>
            <q-btn flat :disable="!checkBaseConf()" v-close-popup @click="saveConfig(model)"> {{ t("common.save") }}</q-btn>
          </q-card-actions>
        </q-card>
      </q-card>
    </q-dialog>

  </q-page-container>
</template>

<script>
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useUserStore} from "@/main/vue/stores/users";
import {reactive, ref, watch} from "vue";
import {useI18n} from 'vue-i18n';
import {storeToRefs} from "pinia";
import {useRoomStore} from "@/main/vue/stores/room";
import DoorConfig from "@/main/vue/views/DoorConfig.vue";
import {getCssVar, useQuasar} from "quasar";
import api from "@/main/vue/api";
import CreateDoor from "@/main/vue/views/CreateDoor.vue";
import {useDoorStore} from "@/main/vue/stores/door";
import {useRoomGroupStore} from "@/main/vue/stores/roomGroupStore";
import {useLocationStore} from "@/main/vue/stores/locations";
import {useConfigStore} from "@/main/vue/stores/config";
import {Direction} from "@/main/vue/entity/doorConfiguration";


export default {
  computed: {
    Direction() {
      return Direction
    }
  },
  components: {DoorConfig},
  props: {
    edit: {
      type: Boolean,
      required: false
    },
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
  methods: {
    getConfig() {
      return this.$refs.doorConfig.qSelects
    },
    getCssVar,
    useRoomGroupStore,
    checkBaseConf() {
      let x = 0;
      let y = false;
      this.qSelectGeneral.qSelectsSet.forEach((element, index) => {
        if (this.qSelectGeneral.qSelectsSet[index].baseConfig === true) {
          x = x + 1;
        }
      })
      if (x === 1) {
        y = true
      }
      return y
    },

    addConfiguration() {
      this.qSelectGeneral.qSelectsSet.push( {
        doorConfigIn: {configParts: [{credentials: [], attributeFilter: [{ attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        doorConfigOut: {configParts: [{credentials: [], attributeFilter: [{attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        baseConfig: false,
        startTime: null,
        endTime: null
      })
    },
    removeConfig(i) {
      this.qSelectGeneral.qSelectsSet.splice(i, 1)
    },
    checkLength() {
      return (this.qSelectGeneral.qSelectsSet.length === 1);
    },


    changeDirectionOut(direction, k) {

      if (direction === Direction.IN) {
        this.$refs.doorOut[k].direction = Direction.OUT
        this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.IN
        this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.OUT

      } else if (direction === Direction.OUT) {
        this.$refs.doorOut[k].direction = Direction.IN
        this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.OUT
        this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.IN

      } else if (direction === Direction.BOTH) {
        this.$refs.doorOut[k].direction = Direction.BOTH
        this.qSelectGeneral.qSelectsSet[k].doorConfigOut.direction = Direction.BOTH
        this.qSelectGeneral.qSelectsSet[k].doorConfigIn.direction = Direction.BOTH

      }
    },
    resetConfig() {
      this.qSelectGeneral.qSelectsSet = [{
        doorConfigIn: {configParts: [{credentials: [], attributeFilter: [{ attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        doorConfigOut: {configParts: [{credentials: [], attributeFilter: [{attribute: null,
              predicateType: null, value: null, currentDate: false}]}], description: "", direction: Direction.BOTH},
        baseConfig: false,
        startTime: null,
        endTime: null
      }]
      this.selectedConfig = null
    },
    saveConfig(model) {
      console.log("model", model)
      const roomGroupStore = useRoomGroupStore()
      const res = []
      for (const val in model) {
        if (val && val !== 0) {
          res.push(model[val])
        }
      }
      let finalArray = res.flat(1).map(d => d.id)
      const allConfig = []
      this.qSelectGeneral.qSelectsSet.forEach((element, index) => {
        let config = {}
        config.baseConfig = this.qSelectGeneral.qSelectsSet[index].baseConfig
        config.startTime = this.qSelectGeneral.qSelectsSet[index].startTime
        config.endTime = this.qSelectGeneral.qSelectsSet[index].endTime
        if (this.$refs.doorIn[index].direction === Direction.BOTH) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
          config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigOut.description
        } else if (this.$refs.doorIn[index].direction === Direction.IN) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigOut))
          config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
          config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigOut.description
        } else if (this.$refs.doorIn[index].direction === Direction.OUT) {
          config.doorConfigIn = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigOut))
          config.doorConfigOut = JSON.parse(JSON.stringify(this.qSelectGeneral.qSelectsSet[index].doorConfigIn))
          config.doorConfigIn.description = this.qSelectGeneral.qSelectsSet[index].doorConfigOut.description
          config.doorConfigOut.description = this.qSelectGeneral.qSelectsSet[index].doorConfigIn.description
        }
        allConfig.push(config)
      })
      console.log("finalArray", finalArray)
      let configList = {
        doorIds: finalArray,
        doorConfig: allConfig
      }
      roomGroupStore.setGroupConfig(configList)
      finalArray = []
    }
  },
  name: "FloorPlanRoomList",

  setup(props, context) {
    const {t} = useI18n();
    const floorPlanStore = useFloorPlanStore()
    const {rooms, selectedRooms} = storeToRefs(floorPlanStore)
    const userStore = useUserStore()
    const doorStore = useDoorStore()
    const roomStore = useRoomStore();
    const filteredRooms = ref([])
    const search = ref()
    const searchGroup = ref()
    const inception = ref();
    const deleteDoorDialog = ref();
    const editDoorDialog = ref();
    const editedDoorName = ref();
    const currentRoomName = ref();
    const $q = useQuasar();

    const roomGroupStore = useRoomGroupStore();
    const selectedGroups = ref([]);
    let filteredGroups = ref([]);
    const newGroup = ref(false);
    const addRoomsToNewGroupDialog = ref(false);
    const currentGroupName = ref();
    const editGroupD = ref(false);
    const numRoomsInGroup = ref();
    const isEditor = ref(false);
    const roomDeleteList = ref([]);

    const model = ref([[]])
    const res = []
    let finalArray = []
    const configStore = useConfigStore()
    configStore.getAllConfigs()
    const {allPreConfigs} = storeToRefs(configStore)
    const configOptions = ref()
    configOptions.value = configStore.allPreConfigs
    const selectedConfig = ref()
    const qSelectGeneral = reactive({
      qSelectsSet: [{
        doorConfigIn: {
          configParts: [{
            credentials: [],
            attributeFilter: [{
              attribute: null,
              predicateType: null,
              value: null,
              currentDate: false
            }]
          }],
          description: "",
          direction: Direction.BOTH
        },
        doorConfigOut: {
          configParts: [{
            credentials: [],
            attributeFilter: [{
              attribute: null,
              predicateType: null,
              value: null,
              currentDate: false
            }]
          }],
          description: "",
          direction: Direction.BOTH
        },
        baseConfig: false,
        startTime: null,
        endTime: null
      }]
    })

    const filterConfigOptions = function (val, update) {
      update(() => {
        const needle = val.toLowerCase()
        configOptions.value = allPreConfigs.value.filter(config => config.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    watch(allPreConfigs, () => {
      configOptions.value = configStore.allPreConfigs
    })

    watch(selectedConfig, async () => {
      if (selectedConfig.value == null) {
        qSelectGeneral.qSelectsSet.splice(0)
        qSelectGeneral.qSelectsSet.push({
          doorConfigIn: {
            configParts: [{
              credentials: [], attributeFilter: [{
                attribute: null,
                predicateType: null, value: null, currentDate: false
              }]
            }], description: "", direction: Direction.BOTH
          },
          doorConfigOut: {
            configParts: [{
              credentials: [], attributeFilter: [{
                attribute: null,
                predicateType: null, value: null, currentDate: false
              }]
            }], description: "", direction: Direction.BOTH
          },
          baseConfig: false,
          startTime: null,
          endTime: null
        })
      } else {
        await configStore.getConfig(selectedConfig.value.id)
        let chosenConfig = configStore.currentConfig
        let tempConfig = []
        chosenConfig?.doorConfig.forEach((element) => {
          console.log(element)
          let object = {
            doorConfigIn: {
              direction: JSON.stringify(element.doorConfigIn)
              !== JSON.stringify(element.doorConfigOut) ? Direction.IN : Direction.BOTH,
              description: element.doorConfigIn.description,
              configParts: element.doorConfigIn.configParts
            },
            doorConfigOut: {
              direction: JSON.stringify(element.doorConfigIn)
              !== JSON.stringify(element.doorConfigOut) ? Direction.OUT : Direction.BOTH,
              description: element.doorConfigOut.description,
              configParts: element.doorConfigOut.configParts
            },
            startTime: element.startTime || '',
            endTime: element.endTime || '',
            baseConfig: element.baseConfig || false
          }
          tempConfig.push(object)
        })
        qSelectGeneral.qSelectsSet = tempConfig
        console.log("temp: ", tempConfig)
        console.log("qselect: ", qSelectGeneral.qSelectsSet)
      }
    })

    const check = (k) => {

      const baseConfCount = qSelectGeneral.qSelectsSet.filter(
          (config) => config.baseConfig
      ).length;
      if (baseConfCount > 1) {
        // Display warning or prevent saving
        console.log('Warning: You can only select one base configuration.');
        $q.notify({
          type: 'negative',
          message: "You can only choose one base configuration.",
          caption: "Error",
          position: "top",
          color: 'negative',
          textColor: 'postitive',
          timeout: 3000,
          classes: "loginNotify"
        })
        qSelectGeneral.qSelectsSet.forEach((element, index) => {
          if (!(index === k)) {
            element.baseConfig = false;
          }
        })
        return false; // Prevent saving
      }
      return true; // Allow saving
    }

    const columns2 = [
      {
        name: 'name',
        required: true,
        label: t('floorPlan.room'),
        align: 'left',
        field: row => row.roomName,
        sortable: true
      },
      {name: 'room', label: t('floorPlan.room'), align: 'left', field: row => row.room, sortable: true},
      {
        name: 'actions',
        label: t('editor.groupRooms.select'),
        style: 'width: 8em',
        headerStyle: 'width: 8em',
        align: 'left'
      },
      {
        name: 'doorNames',
        label: t('editor.groupRooms.doors'),
        field: row => row.doors,
        format: (val) => val.map(e => e.id).join(', '),
        style: 'width: 8em',
        headerStyle: 'width: 8em',
        align: 'left'
      },
      {
        name: 'doors',
        label: t('editor.groupRooms.doors'),
        field: row => row.doors,
        format: (val) => val.map(e => e.id).join(', '),
        style: 'width: 8em',
        headerStyle: 'width: 8em',
        align: 'left'
      }
    ]

    const rows2 = ref([]);
    rows2.value = []

    function getRoomsAndDoors() {
      roomGroupStore.getRoomsAndDoorsByGroupId(selectedGroups.value.id).then((rooms) => {
        rows2.value = rooms.map(r => ({...r, selected: true}))
        fetchDoors(rows2.value)
      })
    }

    function fetchDoors(rows) {
      rows?.forEach(row => getDoors(row.room))
    }

    function getDoors(id) {
      doorStore.getByRoomId(id).then((doors) => {
        model.value[id] = doors
        console.log(model.value)
      })
    }


    function checkIfSelected(room, id) {
      if (!room) {
        model.value[id] = []
      }
    }

    function isEditorCheck() {
      if (userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)) {
        isEditor.value = true;
        console.log("Is Editor Check");
      }

      console.log("Is Editor Check");
      return true;
    }


    isEditorCheck();
    async function start() {
      if (isEditor.value) {
        await roomGroupStore.getRoomGroups();
        console.log("Ist eingeloggt als Editor");
      } else {
        console.log("Ist nicht eingeloggt als Editor");
      }
    }
    start();



    async function loadRoomGroups(buildingID) {
      //filteredGroups.value = [];
      unCheck();
      console.log("loadRoomGroups(): roomGroupStore.filteredGroups", roomGroupStore.filteredGroups);
      roomGroupStore.getGroupByBuilding(buildingID).then(() => {
        filteredGroups.value = [];
        for (const roomG of roomGroupStore.filteredGroups) {
          filteredGroups.value.push(roomG);
          console.log("loadRoomGroups() hier");
        }
        console.log("Groups of rooms (loadRoomGroups)", filteredGroups.value);
        //console.log("sel. Rooms", selectedRooms.value);

      })
    }


    async function makeANewGroup(newGroupName) {
      await checkName(newGroupName);
      //selectedGroups.value = [];
      if (checkNameAllowed.value) {

        await roomGroupStore.makeNewGroup(newGroupName, currentBuilding.value, []);
        await loadRoomGroups(buildingID.value);
        newGroup.value = false;
        console.log("done makeANewGroup");

      }
    }

    const selectedRoomsForGroup = ref([]);
    async function makeIdsToRooms() {
      selectedRoomsForGroup.value = [];
      await roomStore.getRooms();
      //console.log(roomStore.rooms);

      selectedRoomsForGroup.value = roomStore.rooms?.filter((room) =>
          selectedRooms.value.includes(room.id)
      )
      console.log("selectedRoomsForGroup: ", selectedRoomsForGroup.value);

    }

    async function addRoomsToNewGroup() {
      //console.log(selectedRooms.value);
      await checkName(newGroupName.value);
      if (checkNameAllowed.value) {


        await makeIdsToRooms();

        await roomGroupStore.makeNewGroup(newGroupName.value, currentBuilding.value, selectedRoomsForGroup.value);
        await loadRoomGroups(buildingID.value);

        addRoomsToNewGroupDialog.value = false;
        newGroupName.value = "";
        selectedRooms.value = [];
        console.log("done makeANewGroup for selected rooms");

      }
    }

    function checkAddRoomsToNewGroup() {
      if (selectedRooms.value.length === 0) {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noRoomSelected')
        })
      } else {
        addRoomsToNewGroupDialog.value = true;
      }
    }

    function checkIfGroupSelected() {
      console.log("Selected Group for new Group: ", selectedGroups.value);
      if (selectedGroups.value !== null) {
        return true;
      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
        return false;
      }
    }

    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }

    async function addRoomsToGroups() {
      console.log("selected Rooms: ", selectedRooms.value);
      console.log("selected Groups: ", selectedGroups.value);
      //console.log("selected Groups length: ", selectedRooms.value.length);

      if (selectedRooms.value.length === 0) {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noRoomSelected')
        })
      } else if (selectedGroups.value !== null) {

        await makeIdsToRooms();
        const editedGroup = ref({
          id: selectedGroups.value.id,
          name: selectedGroups.value.name,
          building: selectedGroups.value.building,
          rooms: selectedRoomsForGroup.value
        });
        console.log("edited Group:", editedGroup.value);
        await roomGroupStore.editGroup(editedGroup.value);
        console.log("makeIsToRooms before loadRoomGroups: selectedGroups.value", selectedGroups.value)
        await loadRoomGroups(buildingID.value);
        console.log("makeIsToRooms after loadRoomGroups: selectedGroups.value", selectedGroups.value)
        unCheck();
        console.log("makeIsToRooms after unCheck: selectedGroups.value", selectedGroups.value)

      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
      }
    }

    async function deleteGroup() {
      console.log("Delete Group", selectedGroups.value);
      if (selectedGroups.value === []) {
        unCheck();
      }
      if (selectedGroups.value !== null) {
        roomGroupStore.deleteGroup(selectedGroups.value.id).then(async () => {
          await loadRoomGroups(buildingID.value);
          unCheck();
        });
      } else {
        $q.notify({
          type: 'negative',
          message: t('groupRooms.noGroupSelected')
        })
      }
    }

    function checkGroupSelected() {
      if (selectedGroups.value !== null) {
        return true;
      } else {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.noGroupSelected')
        })
        return false;
      }
    }

    const checkNameAllowed = ref(false);

    async function checkName(newName) {
      console.log(newName);
      console.log("room Group List:", filteredGroups.value);
      checkNameAllowed.value = false;
      if (newName.trim() === "") {
        $q.notify({
          type: 'negative',
          message: t('editor.groupRooms.checkNameMessage'),
          caption: t('editor.groupRooms.checkNameCaption')
        })
      } else {
        checkNameAllowed.value = true;
      }
      for (const theGroup of filteredGroups.value) {
        console.log("roomGroup:", theGroup)
        console.log("newName: ", newName);
        if (theGroup.name === newName) {
          $q.notify({
            type: 'negative',
            message: 'Name für dieses Gebäude bereits vergeben',
            caption: 'Bitte wählen Sie einen neuen Namen.'
          })
          checkNameAllowed.value = false;
        }
      }
    }

    const locationStore = useLocationStore();
    const currentBuilding = ref(null);

    function getParentIDs(locations, selectFloorId) {
      for (const location of locations) {
        for (const building of location.buildings) {
          console.log("locations: ", location)
          if (building.floors.some(floor => floor.id === selectFloorId)) {
            currentBuilding.value = building;
            return building.id
          }
        }
      }
      return null;
    }

    let buildingID = ref();
    locationStore.getLocations().then(async (locations) => {
      if (!floorPlanStore.selectedFloorPlan) {
        buildingID.value = locations[0].buildings[0].id
      } else {
        buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
      }
      console.log("buildingId: ", buildingID.value);
      if (isEditor.value) {
        await loadRoomGroups(buildingID.value);
      }
    });


    const {selectedFloorId} = storeToRefs(floorPlanStore)
    let prevBuildingid = null;

    if (isEditor.value) {
      watch(selectedFloorId, async () => {
        locationStore.getLocations().then(async (locations) => {
          if (!floorPlanStore.selectedFloorPlan) {
            buildingID.value = locations[0].buildings[0].id
          } else {
            buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
            if (prevBuildingid !== buildingID.value) {
              console.log("prev B und current b: ", prevBuildingid, buildingID.value);
              await loadRoomGroups(buildingID.value);
              selectedRooms.value = [];
            }
          }
          console.log("buildingId: ", buildingID.value);
          prevBuildingid = buildingID.value;


        });
        //console.log("Orte: ", locations);
        console.log("buildingIds: ", buildingID.value);
      })
    }


    filteredRooms.value = rooms.value

    watch(rooms, () => {
      filteredRooms.value = rooms.value
    })

    function deleteDoor(room, door) {
      const doors = room.doors;
      const index = doors.indexOf(door);
      doors.splice(index, 1);
      room.doors = doors;
      deleteDoorDialog.value = false;
    }

    function openDialog(door) {
      api.door.getById(door.id).then((door) => {
        $q.dialog({
          component: CreateDoor,
          componentProps: {
            door: door.data
          }
        }).onOk(({doorName, configuration}) => {
          door.data.name = doorName
          door.data.doorConfigCmds = configuration
          doorStore.save(door.data).then(() => {
            console.log("success")
          })
        })
      })
    }

    function setOldValueR(room) {
      inception.value = true;
      currentRoomName.value = room.name;
    }

    function setOldValueG(group) {
      editGroupD.value = true;
      currentGroupName.value = group.name;
      selectedGroups.value = group;
      console.log("group:", group);
      console.log("selected Groups in setOldValue: ", selectedGroups.value);
    }

    async function save(room) {
      room.name = currentRoomName.value;
      await roomStore.save(room)
      context.emit('editRoom', room)
    }


    async function editGroupName() {
      console.log("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDd")
      let prevName = selectedGroups.value.name;
      if(prevName !== currentGroupName.value) {
        await checkName(currentGroupName.value);
      }
      else {
        checkNameAllowed.value = true;
      }
      //selectedGroups.value = [];
      if (checkNameAllowed.value) {

        const editedGroup = ref({
          id: selectedGroups.value.id,
          name: currentGroupName.value,
          building: selectedGroups.value.building,
          rooms: selectedGroups.value.rooms
        });
        console.log("edited Group", editedGroup.value);
        roomGroupStore.editGroup(editedGroup.value).then(async () => {
          console.log("editGroupName(); selectedGroup.value vor loadRoomGroups", selectedGroups.value); //richtig
          await loadRoomGroups(buildingID.value);
          console.log("editGroupName(); selectedGroup.value nach loadRoomGroups", selectedGroups.value);
          unCheck();
          console.log("editGroupName(); selectedGroup.value nach unCheck", selectedGroups.value);
          //await deleteRoomsOfGroup();
        });
      }

    }

    function addRoom(element) {
      selectedRooms.value.push(element)
    }

    function deleteRoom(element) {
      selectedRooms.value.forEach((item, index) => {
        if (item === element) selectedRooms.value.splice(index, 1);
      });
    }

    function updateNumRoomsInGroup() {
      if (selectedGroups.value !== null) {
        numRoomsInGroup.value = selectedGroups.value.rooms.length;
        console.log("NumRoomsInGroup: ", numRoomsInGroup.value);
      }
    }

    function toggleRoomCheckbox(element) {
      //console.log(element)
      if (selectedRooms.value.some(e => e === element.id)) {
        deleteRoom(element.id)
      } else {
        addRoom(element.id)
      }
      //console.log("selected Rooms toggle:", selectedRooms.value);
    }

    async function roomFilter() {
      if (!search.value || search.value.trim() === '') {
        filteredRooms.value = floorPlanStore.rooms
      } else if (search.value) {
        const request = search.value.toLowerCase().trim()
        filteredRooms.value = floorPlanStore.rooms.filter(room => {
          return room.name.toLowerCase().includes(request)
        })
      }
    }

    async function groupFilter() {
      if (!searchGroup.value || searchGroup.value.trim() === '') {
        filteredGroups.value = roomGroupStore.filteredGroups;
      } else if (searchGroup.value) {
        const request = searchGroup.value.toLowerCase().trim()
        filteredGroups.value = roomGroupStore.filteredGroups.filter(group => {
          return group.name.toLowerCase().includes(request)
        })
      }
    }

    function unCheck() {
      selectedGroups.value = null
      selectedRooms.value = [];
    }

    const prevSelectedGroup = ref();

    function filterRoomToGroups() {
      if (prevSelectedGroup.value === null || prevSelectedGroup.value === undefined
          || (prevSelectedGroup.value !== selectedGroups.value)) {
        console.log("prevSelectedGroup.value === null");
        selectedGroups.value.rooms.forEach((room) => {
          //console.log("Raum: ", room);
          toggleRoomCheckbox(room);
        })
        //console.log("selected Rooms after selecting Group:", selectedRooms.value);
      } else {
        console.log("else", prevSelectedGroup.value);
        // to un-toggle the selected rooms
        selectedGroups.value.rooms.forEach((room) => {
          //console.log("Raum (untoggle): ", room);
          toggleRoomCheckbox(room);
        })
        //console.log("sel. rooms after un-toggle", selectedRooms.value);
        unCheck();
      }

      console.log("Function: filterRoomsToGroups()");
      prevSelectedGroup.value = selectedGroups.value;
    }

    const newGroupName = ref('');

    const arrayFloors = ref([]);

    async function allFloorsForGroup() { //ab hier schon zu viele Räume drin (Bugfixing)
      const rooms = selectedGroups.value.rooms;
      arrayFloors.value = [];

      for (let roomLength = 0; roomLength < rooms.length; roomLength++) {

        await roomStore.getFloor(rooms[roomLength].id);
        let level = roomStore.floor.floorLevel
        arrayFloors.value.push(level);
        //console.log("Room:", rooms[roomLength], "Floor", level);
      }
      console.log("arrayFloors:", arrayFloors.value);
      console.log("function: allFloorsForGroup()");
    }

    function addToDeleteList(room) {
      roomDeleteList.value.push(room);
      console.log("add to delete list: room:", room);
      let i = 0;
      for (const roomFromGroup of selectedGroups.value.rooms) {
        console.log("For");
        if ((i === 0) && (room.id === roomFromGroup.id) ) {
          selectedGroups.value.rooms.shift();
        } else if (room.id === roomFromGroup.id) {
          console.log("i: ", i);
          //selectedGroups.value.rooms.pop(roomFromGroup);
          selectedGroups.value.rooms.splice(i, 1);
          console.log("ohne ", selectedGroups.value.rooms);
        }
        i++;
      }
      console.log("entire delete-list:", roomDeleteList.value);
      console.log("selected Group rooms:", selectedGroups.value.rooms);
    }
    const dropdown = ref(false);

    return {
      check,
      model,
      qSelectGeneral,
      fetchDoors,
      visibleColumns: ref(['name', 'actions']),
      rows2,
      searchinput: ref(''),
      columns2,
      val: ref(true),
      prompt: ref(false),
      getPaginationLabel,
      filterConfigOptions,
      configOptions,
      selectedConfig,
      getRoomsAndDoors,
      finalArray,
      getDoors,
      checkIfSelected,
      floorPlanStore,
      selectedRooms,
      toggleRoomCheckbox,
      userStore,
      search,
      searchGroup,
      filteredRooms,
      roomFilter,
      groupFilter,
      t,
      inception,
      deleteDoorDialog,
      editedDoorName,
      editDoorDialog,
      openDialog,
      roomStore,
      deleteDoor,
      save,
      editGroupName,
      currentRoomName,
      currentGroupName,
      setOldValueR,
      setOldValueG,
      tab: ref('rooms'),
      allGroups: filteredGroups,
      selectedGroups,
      newGroup,
      addRoomsToNewGroupDialog,
      addRoomsToExistingGroupDialog: ref(false),
      newGroupName,
      makeANewGroup,
      addRoomsToGroups,
      checkAddRoomsToNewGroup,
      checkIfGroupSelected,
      addRoomsToNewGroup,
      unCheck,
      deleteGroup,
      checkGroupSelected,
      editGroupD,
      deleteAlert: ref(false),
      filterRoomToGroups,
      numRoomsInGroup,
      updateNumRoomsInGroup,
      delName() {
        newGroupName.value = "";
      },
      selectGroup(group) {
        selectedRooms.value = [];
        selectedGroups.value = group;
      },
      giveLog(){
        dropdown.value = false;
        //console.log("hiwerhoiehfowgiohowhoifwohfoweihfiosiohfi");
        //console.log(dropdown.value);
      },
      allFloorsForGroup,
      dropdown,
      arrayFloors,
      addToDeleteList,
      async reloadRoomsBE() {
        if(selectedGroups.value !== null) {
          console.log("sel. Id", selectedGroups.value.id);
          await roomGroupStore.getRoomsByGroupId(selectedGroups.value.id);
          console.log("hier", roomGroupStore.rooms);
          selectedGroups.value.rooms = roomGroupStore.rooms;
          console.log("reloadRoomsBE(), selectedGroups", selectedGroups.value);
        }
      },
      async cancelEdit(){
        await loadRoomGroups(buildingID.value);
      }
    }
  },

}
</script>

<style scoped>

</style>