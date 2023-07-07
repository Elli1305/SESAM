<template>
    <q-page-container class="no-padding no-margin">
        <q-page style="padding-right: 1em; padding-top: 2em">

            <div v-if="userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)">


                <q-tabs
                        v-model="tab"
                        no-caps
                        class="bg-primary text-white shadow-2"
                >
                    <q-icon size="1.25em" fixed-right color="white" name="info_outlined" class="q-pl-xs">
                        <q-tooltip class="grey" anchor="bottom right" max-width="200px" self="top middle"
                                   :offset="[0, 0]">
                            {{ t('editor.groupRooms.info') }}
                        </q-tooltip>
                    </q-icon>
                    <q-tab name="rooms" :label="t('floorPlan.rooms')"/>
                    <q-tab name="groups" :label="t('editor.groupRooms.groups')"/>

                </q-tabs>
                <q-separator></q-separator>

                <q-tab-panels v-model="tab" animated style="max-width: 24em; min-width: 24em">
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
                        <q-scroll-area style="height: 30em; min-width: 21em; overflow: hidden; max-width: 22em; "
                                       :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
                        >
                            <q-list>
                                <q-item v-for="room in filteredRooms" style="padding-left: 0">
                                    <q-checkbox v-model="selectedRooms" :val="room"
                                                color="blue"/>
                                    <q-btn-dropdown
                                            split
                                            flat
                                            style="min-width: 18em"
                                            :label="room.name"
                                            dropdown-icon="expand_more"
                                            color="var(--text-color)"
                                            @click="toggleRoomCheckbox(room);">
                                        <div class="column no-wrap" style="background-color: var(--bg-color)">
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
                                                                                                   :label="t( 'floorplan.cancel')"
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
                                                                    <q-btn flat :label="t( 'floorPlan.cancel')"
                                                                           color="primary" v-close-popup/>
                                                                    <q-btn flat :label="t( 'floorPlan.save')"
                                                                           color="primary" @click="save(room)"
                                                                           v-close-popup/>
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
                            <q-item>
                                <q-btn style="min-width: 10em" color="primary" icon="add"
                                       :label="t('editor.groupRooms.addRoomsToNewGroup')"
                                       @click="checkAddRoomsToNewGroup();" flat/>
                                <q-btn style="min-width: 10em" color="blue" icon="add"
                                       :label="t('editor.groupRooms.addRooms')"
                                       @click="addRoomsToExistingGroupDialog=checkIfGroupSelected();" flat/>
                            </q-item>


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
                        <q-scroll-area style="height: 50vh; min-width: 21em; overflow: hidden; max-width: 22em;" @scroll="giveLog();dropdown = false"
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

                                        <div class="column no-wrap" style="background-color: var(--bg-color)">
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
                                                    <q-dialog v-model="editGroupD">
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
                                                                           :label="t('editor.groupRooms.doorconfiguration')" @click="" flat/>

                                                                    <q-btn flat :label="t( 'common.cancel')"
                                                                           color="primary"
                                                                           v-close-popup/>
                                                                    <q-btn flat :label="t( 'common.save')"
                                                                           color="primary"
                                                                           @click="editGroupName;  deleteRoomsOfGroup();updateNumRoomsInGroup();" v-close-popup/>
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
                            <q-item>
                                <q-btn color="primary" icon="add" :label="t('editor.groupRooms.newGroup')"
                                       @click="delName(); newGroup = true;" flat/>
                                <q-btn
                                        dense
                                        round
                                        flat
                                        icon="delete"
                                        color="grey"
                                        :label="t('common.delete')"
                                        @click="deleteAlert = checkGroupSelected();"/>
                            </q-item>


                        </div>

                    </q-tab-panel>
                </q-tab-panels>
            </div>


            <div v-else>


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
                    <q-scroll-area style=" min-width: 21em; height: 30em; max-width: 22em; "
                                   :horizontal-thumb-style="{ right: '4px', borderRadius: '5px', background: 'red', width: '10px', opacity: 0,  }"
                    >
                        <q-list>
                            <q-item v-for="room in filteredRooms" style="padding-left: 0">
                                <q-checkbox v-model="selectedRooms" :val="room" color="blue"/>
                                <q-btn-dropdown
                                        split
                                        flat
                                        style="min-width: 16em"
                                        :label="room.name"
                                        dropdown-icon="expand_more"
                                        color="var(--text-color)"
                                        @click="toggleRoomCheckbox(room)">
                                    <div class="column no-wrap" style="background-color: var(--bg-color)">
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
                                                                                               :label="t( 'floorPlan.cancel')"
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
                                                                <q-btn flat :label="t( 'floorPlan.cancel')"
                                                                       color="primary"
                                                                       v-close-popup/>
                                                                <q-btn flat :label="t( 'floorPlan.save')"
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

    </q-page-container>
</template>

<script>
import {useFloorPlanStore} from "@/main/vue/stores/floorPlan";
import {useUserStore} from "@/main/vue/stores/users";
import {ref, watch} from "vue";
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


export default {
    components: {DoorConfig},
    props: {
        edit: {
            type: Boolean,
            required: false
        }
    },
    methods: {
        getConfig() {
            return this.$refs.doorConfig.qSelects
        },
        getCssVar
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

        function isEditorCheck() {
            if (userStore.authenticated && userStore.user.roles.some(r => r.role === 'EDITOR' && r.granted)) {
                isEditor.value = true;
                console.log("Is Editor Check");
            }

            console.log("Is Editor Check");
            return true;
        }


        isEditorCheck();
        if (isEditor.value) {
            roomGroupStore.getRoomGroups();
            console.log("Ist eingeloggt als Editor");
        } else {
            console.log("Ist nicht eingeloggt als Editor");
        }


        async function loadRoomGroups(buildingID) {
            filteredGroups.value = [];
            unCheck();
            await roomGroupStore.getGroupByBuilding(buildingID).then(() => {
                filteredGroups.value = [];
                for (const roomG of roomGroupStore.filteredGroups) {
                    filteredGroups.value.push(roomG);
                    console.log("hier");
                }
                console.log("Groups of rooms (loadRoomGroups)", filteredGroups.value);
                console.log("sel. Rooms", selectedRooms.value);


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

        async function addRoomsToNewGroup() {
            console.log(selectedRooms.value);
            await checkName(newGroupName.value);
            if (checkNameAllowed.value) {
                await roomGroupStore.makeNewGroup(newGroupName.value, currentBuilding.value, selectedRooms.value);
                await loadRoomGroups(buildingID.value);

                addRoomsToNewGroupDialog.value = false;
                newGroupName.value = "";
                console.log("done makeANewGroup for selected rooms");

            }
        }

        function checkAddRoomsToNewGroup() {
            if (selectedRooms.value.length === 0) {
                $q.notify({
                    type: 'negative',
                    message: t('groupRooms.noRoomSelected')
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
                    message: t('groupRooms.noGroupSelected')
                })
                return false;
            }
        }

        async function addRoomsToGroups() {
            console.log("selected Rooms: ", selectedRooms.value);
            console.log("selected Groups: ", selectedGroups.value);
            console.log("selected Groups length: ", selectedRooms.value.length);

            if (selectedRooms.value.length === 0) {
                $q.notify({
                    type: 'negative',
                    message: t('editor.groupRooms.noRoomSelected')
                })
            } else if (selectedGroups.value !== null) {
                const editedGroup = ref({
                    id: selectedGroups.value.id,
                    name: selectedGroups.value.name,
                    building: selectedGroups.value.building,
                    rooms: selectedRooms.value
                });
                console.log(editedGroup.value);
                await roomGroupStore.editGroup(editedGroup.value);
                await loadRoomGroups(buildingID.value);
                unCheck();
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
                await roomGroupStore.deleteGroup(selectedGroups.value.id).then(() => {
                    loadRoomGroups(buildingID.value);
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
        locationStore.getLocations().then((locations) => {
            if (!floorPlanStore.selectedFloorPlan) {
                buildingID.value = locations[0].buildings[0].id
            } else {
                buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
            }
            console.log("buildingId: ", buildingID.value);
            if (isEditor.value) {
                loadRoomGroups(buildingID.value);
            }
        });


        const {selectedFloorId} = storeToRefs(floorPlanStore)

        if (isEditor.value) {
            watch(selectedFloorId, () => {
                locationStore.getLocations().then((locations) => {
                    if (!floorPlanStore.selectedFloorPlan) {
                        buildingID.value = locations[0].buildings[0].id
                    } else {
                        buildingID.value = getParentIDs(locations, floorPlanStore.selectedFloorId);
                    }
                    console.log("buildingId: ", buildingID.value);
                    loadRoomGroups(buildingID.value);

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
            api.doorConfig.get(door.name + '_' + door.id).then((config) => {
                $q.dialog({
                    component: CreateDoor,
                    componentProps: {
                        door: door,
                        doorConfigIn: config.data.doorConfigIn,
                        doorConfigOut: config.data.doorConfigOut
                    }
                }).onOk(({doorName, configuration}) => {
                    door.name = doorName
                    door.proofConfigIn = [configuration.doorConfigIn]
                    door.proofConfigOut = [configuration.doorConfigOut]
                    doorStore.save(door).then((savedDoor) => {
                        configuration.doorConfigIn.doorId = savedDoor.name + '_' + savedDoor.id + '_in'
                        configuration.doorConfigOut.doorId = savedDoor.name + '_' + savedDoor.id + '_out'
                        api.doorConfig.save(configuration).then(() => console.log("success"))
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

        function save(room) {
            room.name = currentRoomName.value;
            roomStore.save(room)
            context.emit('editRoom', room)
        }

        async function editGroupName() {
            await checkName(currentGroupName.value);
            //selectedGroups.value = [];
            if (checkNameAllowed.value) {

                const editedGroup = ref({
                    id: selectedGroups.value.id,
                    name: currentGroupName.value,
                    building: selectedGroups.value.building,
                    rooms: selectedGroups.value.rooms
                });
                console.log(editedGroup.value);
                await roomGroupStore.editGroup(editedGroup.value).then(() => {
                    loadRoomGroups(buildingID.value);
                    unCheck();
                });
            }
        }

        function addRoom(element) {
            selectedRooms.value.push(element)
        }

        function deleteRoom(element) {
            selectedRooms.value.forEach((item, index) => {
                if (item.id === element.id) selectedRooms.value.splice(index, 1);
            });
        }

        function updateNumRoomsInGroup() {
            if (selectedGroups.value !== null) {
                numRoomsInGroup.value = selectedGroups.value.rooms.length;
                console.log("NumRoomsInGroup: ", numRoomsInGroup.value);
            }
        }

        function toggleRoomCheckbox(element) {
            console.log(element)
            if (selectedRooms.value.some(e => e.id === element.id)) {
                deleteRoom(element)
            } else {
                addRoom(element)
            }
            console.log("selected Rooms toggle:", selectedRooms.value);
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
        }

        const prevSelectedGroup = ref();

        function filterRoomToGroups() {
            if (prevSelectedGroup.value === null || prevSelectedGroup.value === undefined
                || (prevSelectedGroup.value !== selectedGroups.value)) {
                console.log("prevSelectedGroup.value === null");
                selectedGroups.value.rooms.filter((room) => {
                    //console.log("Raum: ", room);
                    toggleRoomCheckbox(room);
                })
                //console.log("selected Rooms after selecting Group:", selectedRooms.value);
            } else {
                console.log("else", prevSelectedGroup.value);
                // to un-toggle the selected rooms
                selectedGroups.value.rooms.filter((room) => {
                    //console.log("Raum: ", room);
                    toggleRoomCheckbox(room);
                })
                //console.log("sel. rooms after un-toggle", selectedRooms.value);
                unCheck();
            }

            prevSelectedGroup.value = selectedGroups.value;
        }

        const newGroupName = ref('');

        const arrayFloors = ref([]);

        async function allFloorsForGroup() {
            const rooms = selectedGroups.value.rooms;
            arrayFloors.value = [];

            for (let roomLength = 0; roomLength < rooms.length; roomLength++) {

                await roomStore.getFloor(rooms[roomLength].id);
                let level = roomStore.floor.floorLevel
                arrayFloors.value.push(level);
                console.log("Room:", rooms[roomLength], "Floor", level);
            }
            console.log("arrayFloors:", arrayFloors.value);
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
        async function deleteRoomsOfGroup() {
            await roomGroupStore.editGroup(selectedGroups.value);
        }
        const dropdown = ref(false);

        return {
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
            deleteRoomsOfGroup,
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
                selectedGroups.value = group;
            },
            giveLog(){
                dropdown.value = false;
              console.log("hiwerhoiehfowgiohowhoifwohfoweihfiosiohfi");
              console.log(dropdown.value);
            },
            allFloorsForGroup,
            dropdown,
            arrayFloors,
            addToDeleteList,
            async reloadRoomsBE() {
                console.log("sel. Id", selectedGroups.value.id);
                await roomGroupStore.getRoomsByGroupId(selectedGroups.value.id);
                console.log("hier", roomGroupStore.rooms);
                selectedGroups.value.rooms = roomGroupStore.rooms;
            }
        }
    },

}
</script>

<style scoped>

</style>