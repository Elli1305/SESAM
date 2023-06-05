<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{t('issuermanagement.title')}}</p>
    <div class="row self-center" style="display: flex">
      <q-table
          style="width: 80vw; height: 50vh"
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
            <q-input dense borderless debounce="250" v-model="filter" :placeholder="t('issuermanagement.search')">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
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
      <q-dialog v-model="prompt" persistent>
      <q-card>
        <q-card-section>
          <div class="text-h6">{{t('issuermanagement.dialogTitle')}}</div>
        </q-card-section>
        <q-card-section>
          <q-form style="width: 20em" @submit="saveChanges">
            <q-input class="q-my-sm" v-model="model1" label="Last Name" outlined readonly></q-input>
            <q-input class="q-my-sm" v-model="model2" label="First Name" outlined readonly></q-input>
            <q-select class="q-my-sm"
                filled
                v-model="model4"
                multiple
                :label="t('issuermanagement.credentialsList')"
                emit-value
                :options="credentialStore.allCredentials"
                option-label="name"
                option-value="id"
                map-options
                options-cover/>
            <q-select class="q-my-sm"
                filled
                v-model="model3"
                :label="t('issuermanagement.roomsList')"
                emit-value
                :options="roomStore.rooms"
                option-label="name"
                option-value="id"
                map-options
                options-cover/>
          </q-form>
        </q-card-section>
        <q-card-actions align="right" class="text-primary">
          <q-btn flat  v-close-popup :label="t('issuermanagement.cancel')" color="primary"/>
          <q-btn flat v-close-popup :label="t('issuermanagement.save')" color="primary" class="q-ml-md" @click="editIssuers(id)"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import {ref} from 'vue';
import {useI18n} from 'vue-i18n';
import {useCredentialStore} from "@/main/vue/stores/credential"
import {useRoomStore} from "@/main/vue/stores/room";
import {useUserStore} from "@/main/vue/stores/users";


export default {
  setup() {
    const rows = ref([]);
    const searchQuery = ref('');
    const model1 = ref('');
    const model2 = ref('');
    const model3 = ref();
    const model4 = ref([]);



    const {t} = useI18n();
    const credentialStore = useCredentialStore()
    const roomStore = useRoomStore()
    const userStore = useUserStore();
    const id = ref();

    userStore.getIssuers().then((issuers) => {
        rows.value = issuers
    })

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
      id.value = row.id;
      model1.value = row.lastName;
      model2.value = row.firstName;
      model3.value = row.room;
      model4.value = row.credentials.map(c => c.id);
      editedRow.value = {...row};
    };


    const editedRow = ref({});

    function getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }


    credentialStore.getCredentials().then((external) => {
    })

    credentialStore.getCredentials().then((external) => {})
    roomStore.getRooms().then((rooms) =>{})



    function editIssuers(id){
        userStore.updateIssuer(id, model4.value, model3.value)
        this.timeout= setTimeout(() =>
            userStore.getIssuers().then((issuers) => {
                rows.value = issuers
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
      roomStore,
      editIssuers,
      prompt: ref(false),
      userStore,
      id,
      getPaginationLabel,
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