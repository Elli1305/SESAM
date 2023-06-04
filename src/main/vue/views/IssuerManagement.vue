<template>
    <q-page>
      <div class="q-pa-md">
        <div class="q-mb-xl">
          <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t('issuermanagement.title') }}</h1>
        </div>
        <div class="items-center justify-center" style="display: flex">
          <div class="center" style="max-width: 80em; min-width: 60em">
            <q-table :rows="rows" :columns="columns" row-key="username" :separator="'cell'" :filter="filter">
              <template v-slot:top-right="props">
                <div class="q-pa-md">
                  <q-input dense debounce="300" v-model="filter" :placeholder="t('issuermanagement.search')">
                    <template v-slot:append>
                      <q-icon name="search" />
                    </template>
                  </q-input>
                </div>
              </template>
              <template v-slot:body-cell-actions="props">
                <q-td :props="props">
                  <q-btn dense round flat color="grey" @click="prompt=true; openForm(props.row)" icon="edit"></q-btn>
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
        </div>
          <q-dialog v-model="prompt" persistent>
          <q-card class="form-card">
            <q-card-section>
              <h2>{{ t('issuermanagement.dialogTitle') }}</h2>
              <q-form @submit="saveChanges">
                <q-input v-model="model1" label="Last Name" outlined readonly style="padding-bottom: 1em"></q-input>
                <q-input v-model="model2" label="First Name" outlined readonly style="padding-bottom: 1em"></q-input>
                <q-select
                    style="padding-bottom: 1em"
                    filled
                    v-model="model4"
                    multiple
                    :label="t('issuermanagement.credentialsList')"
                    emit-value
                    :options="credentialStore.allCredentials"
                    option-label="name"
                    option-value="id"
                    options-cover
                    map-options
                ></q-select>
                <q-select
                    filled
                    v-model="model3"
                    :label="t('issuermanagement.roomsList')"
                    emit-value
                    :options="roomStore.rooms"
                    option-label="name"
                    option-value="id"
                    options-cover
                    map-options
                ></q-select>


                <q-card-actions align="right">
                  <q-btn :label="t('issuermanagement.cancel')" color="primary"/>
                  <q-btn type="submit" :label="t('issuermanagement.save')" color="primary" class="q-ml-md" @click="editIssuers(editedRow.id)"/>
                </q-card-actions>
              </q-form>
            </q-card-section>
          </q-card>
        </q-dialog>
      </div>
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
    const model1 = ref('');
    const model2 = ref('');
    const model3 = ref([]);
    const model4 = ref([]);

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

    const openForm = (row) => {
      model1.value = row.lastName;
      model2.value = row.firstName;
      model3.value = row.room.map(c => c.id);
      model4.value = row.credentials.map(c => c.id);
      editedRow.value = {...row};
    };


    const editedRow = ref({});


    credentialStore.getCredentials().then((external) => {
    })

    credentialStore.getCredentials().then((external) => {})
    roomStore.getRooms().then((rooms) =>{})



    function editIssuers(id){
        userStore.updateIssuer(id, model4, model3)
        this.timeout= setTimeout(() => axios.get('api/issuers').then((res) => {
            rows.value = res.data;
        }), 250)
    }



    return {
      model1,
      model2,
      model3,
      model4,
      columns,
      searchQuery,
      credentialStore,
      t,
      editedRow,
      openForm,
      rows,
      filter,
      editedItem,
      roomStore,
      editIssuers,
      prompt: ref(false)

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