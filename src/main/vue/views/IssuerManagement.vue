<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('issuermanagement.title')}}</p>
    <div class="row self-center" style="display: flex">
      <q-table
          style="width: 75vw; height: 25em"
          :rows-per-page-options="[0]"
          :rows="rows"
          :columns="columns"
          :title="t('profile.issuer')"
          :separator="'cell'"
          :no-data-label="t('common.noData')"
          :no-results-label="t('common.noResults')"
          :pagination-label="getPaginationLabel"
          :filter="filter"
          row-key="username">
        <template v-slot:top-right>
          <div class="q-pa-md">
            <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('issuermanagement.search')">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
          </div>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" @click="openForm(props.row)" icon="edit"></q-btn>
          </q-td>
        </template>

        <template v-slot:body-cell-roles="props">
          <q-td :props="props">
            <div>
              {{ props.row.credential.map(option => option).join(',') }}
            </div>
          </q-td>
        </template>

        <template v-slot:body-cell-roomId="props">
          <q-td :props="props">
            <div>{{ props.row.room.name }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-crendetials="props">
          <q-td :props="props">
            <div>{{ props.row.credentials }}</div>
          </q-td>
        </template>
      </q-table>
    </div>
    <q-dialog v-model="isFormOpen">
      <q-card>
        <q-card-section>
          <div class="text-h6">{{t('issuermanagement.dialogTitle')}}</div>
        </q-card-section>
        <q-card-section>
          <q-form style="width: 20em" @submit="saveChanges">
            <q-input class="q-my-sm" v-model="editedRow.lastName" label="Last Name" outlined readonly></q-input>
            <q-input class="q-my-sm" v-model="editedRow.firstName" label="First Name" outlined readonly></q-input>
            <q-select class="q-my-sm"
                filled
                v-model="editedRow.credential"
                multiple
                :label="t('issuermanagement.credentialsList')"
                emit-value
                :options="credentialStore.allCredentials"
                option-label="name"
                option-value="id"
                options-cover/>
            <q-select class="q-my-sm"
                filled
                v-model="editedRow.room"
                multiple
                :label="t('issuermanagement.roomsList')"
                emit-value
                :options="roomStore.rooms"
                option-label="name"
                option-value="id"
                options-cover/>
          </q-form>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat  :label="t('issuermanagement.cancel')" color="primary" @click="closeForm"/>
          <q-btn flat type="submit" :label="t('issuermanagement.save')" color="primary" class="q-ml-md" @click="confirmSave"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import {ref} from 'vue';
import {useI18n} from 'vue-i18n';
import axios from 'axios';
import {Dialog} from 'quasar';
import {useCredentialStore} from "@/main/vue/stores/credential"
import {useRoomStore} from "@/main/vue/stores/room";
import {useUserStore} from "@/main/vue/stores/users";


export default {
  setup() {
    const rows = ref([]);
    const searchQuery = ref('');
    const isFormOpen = ref(false);
    const editedItem = ref(null);
    const id = ref(null);
    const credential = ref([]);
    const room = ref('');

    axios.get('api/issuers').then((res) => {
      rows.value = res.data;
    });



    const {t} = useI18n();
    const credentialStore = useCredentialStore()
    const roomStore = useRoomStore()
    const userStore = useUserStore();

    const filter=ref('')
    const columns = [
      {
        name: 'firstName',
        align: 'center',
        label: t('issuermanagement.firstname'),
        field: 'firstName',
        sortable: true,
      },
      {
        name: 'lastName',
        required: true,
        label: t('issuermanagement.lastname'),
        align: 'center',
        field: (row) => row.lastName,
        format: (val) => `${val}`,
        sortable: true,
      },
      {
        name: 'credentials',
        align: 'center',
        label: t('issuermanagement.credentials'),
        field: 'credentials',
        format: (val) => val.map((credential) => credential.name).join(', '),
        sortable: true,
      },

      {
        name: 'roomId',
        align: 'center',
        label: t('issuermanagement.roomId'),
        field: 'room.name',
        sortable: true,
      },
      {name: 'actions', label: t('issuermanagement.edit'), style: 'width: 40px', align: 'center'},
    ];


    const confirmSave = () => {
      Dialog.create({
        message: t('issuermanagement.saveConfirmationMessage'),
        persistent: true,
        ok: {
          label: t('issuermanagement.save'),
          color: 'primary',
          classes: 'q-ma-sm',
        },
        cancel: {
          label: t('issuermanagement.cancel'),
          color: 'primary',
          classes: 'q-ma-sm',
        },
      })
          .onOk(() => {
            saveChanges();
          })
          .onCancel(() => {
            isFormOpen.value = false;
          });
    };
    const closeForm = () => {
      isFormOpen.value = false;
    };
    const openForm = (row) => {
      editedItem.value = { ...row };
      id.value = row.id;

      room.value = row.room.name;
      isFormOpen.value = true;
    };


    const editedRow = ref({
      room: '',
      credential: [],
    });



    const saveChanges = () => {
      const index = rows.value.findIndex((row) => row.id === editedItem.value.id);
      if (index !== -1) {
        const updatedRow = {
          ...rows.value[index],
          credential: editedRow.value.credential,
          room: { name: editedRow.value.room },
        };
        rows.value.splice(index, 1, updatedRow);
      }

      isFormOpen.value = false;
    };

    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }


    credentialStore.getCredentials().then((external) => {
    })

    credentialStore.getCredentials().then((external) => {})
    roomStore.getRooms().then((rooms) =>{})




    const updateIssuer = () => {
      if (!id.value) {
        console.error('No issuer ID provided');
        return;
      }

      userStore
          .updateIssuer(id.value, credential.value, room.value)
          .then(() => {
            const updatedRows = rows.value.map((row) => {
              if (row.id === id.value) {
                return {
                  ...row,
                  credential: credential.value,
                  room: { name: room.value },
                };
              }
              return row;
            });
            rows.value = updatedRows;
            isFormOpen.value = false;
          })
          .catch((error) => {
            console.error('Failed to update issuer:', error);
          });
    };



    return {
      columns,
      searchQuery,
      credentialStore,
      t,
      editedRow,
      isFormOpen,
      openForm,
      saveChanges,
      rows,
      filter,
      editedItem,
      confirmSave,
      closeForm,
      roomStore,
      updateIssuer,
      getPaginationLabel
    };
  },
};
</script>

<style scoped>


.form-card {
  width: 100%;
  max-width: 500px;
}
</style>