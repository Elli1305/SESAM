<template>
  <div class="q-pa-md">
    <div class="q-mb-xl">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t('issuermanagement.title') }}  </h1>
    </div>
    <div class="items-center justify-center" style="display: flex">
      <div class="center" style="max-width: 80em; min-width: 60em">
        <q-input v-model="searchQuery" dense outlined placeholder="Search" class="q-mb-md" @input="filterRows">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>

        <q-table :rows="filteredRows" :columns="columns" row-key="username" :separator="'cell'">
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
    </div>
    <q-dialog v-model="isFormOpen" content-class="form-dialog">
      <q-card class="form-card">
        <q-card-section>
          <h2>Edit User</h2>
          <q-form @submit="saveChanges">
            <q-input v-model="editedRow.lastName" label="Last Name" outlined readonly></q-input>
            <q-input v-model="editedRow.firstName" label="First Name" outlined readonly></q-input>
            <q-input v-model="editedRow.room.name" label="Room Name" outlined readonly></q-input>
            <q-select
                filled
                v-model="editedRow.category"
                multiple
                :label="t('issuermanagement.list')"
                emit-value
                :options="credentialStore.allCredentials"
                option-label="name"
                options-cover
            ></q-select>

            <q-card-actions align="right">
              <q-btn label="Cancel" color="primary" @click="closeForm"/>
              <q-btn type="submit" label="Save" color="primary" class="q-ml-md" @click="confirmSave"/>
            </q-card-actions>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import {ref, watchEffect} from 'vue';
import {useI18n} from 'vue-i18n';
import axios from 'axios';
import {Dialog} from 'quasar';
import {useCredentialStore} from "@/main/vue/stores/credential";


export default {
  name: 'IssuerManagement',

  setup() {
    const rows = ref([]);
    const searchQuery = ref('');

    const isFormOpen = ref(false);
    const editedItem = ref(null); // Separate edited item object
    axios.get('api/issuers').then((res) => {
      rows.value = res.data;
    });

    const {t} = useI18n();
    const credentialStore = useCredentialStore()


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
      { name: 'actions', label: t('issuermanagement.edit'), style: 'width: 40px', align: 'center' },
    ];


    const filteredRows = ref([]);

    const filterRows = () => {
      filteredRows.value = rows.value.filter((row) =>
          row.roles.some((role) => role.role === 'ISSUER')
      ).filter((row) =>
          row.lastName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          row.firstName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          row.username.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          row.credential.some((option) => option.includes(searchQuery.value.toLowerCase()))
      );
    };

    const confirmSave = () => {
      Dialog.create({
        title: 'Confirm Save',
        message: 'Are you sure you want to save the changes?',
        persistent: true,
        ok: {
          label: 'Save',
          color: 'primary',
          classes: 'q-ma-sm',
        },
        cancel: {
          label: 'Cancel',
          color: 'primary',
          classes: 'q-ma-sm',
        },
      })
          .onOk(() => {
            saveChanges();
          })
          .onCancel(() => {
            isFormOpen.value = false; // Close the dialog without making changes
          });
    };
    const closeForm = () => {
      isFormOpen.value = false;
    };
    const openForm = (row) => {
      editedItem.value = {...row}; // Assign the selected item to editedItem
      editedRow.value = {...row}; // Assign the selected item to editedRow
      isFormOpen.value = true;
    };


    const editedRow = ref({
      // other properties...
      credential: [], // Initialize as an empty array for multiple selection
    });

    // ...

    const saveChanges = () => {
      const index = rows.value.findIndex((row) => row.username === editedItem.value.username);
      if (index !== -1) {
        // Update the category property as an array of selected options
        const updatedRow = {...rows.value[index], category: editedRow.value.category};
        rows.value.splice(index, 1, updatedRow);
      }

      isFormOpen.value = false;

      // Perform any additional logic or API calls to save the changes
    };



    credentialStore.getCredentials().then((external) => {})


    watchEffect(filterRows);

    return {
      columns,
      filteredRows,
      searchQuery,
      credentialStore,
      t,
      editedRow,
      isFormOpen,
      openForm,
      saveChanges,
      rows,
      editedItem,
      confirmSave,
      closeForm,
    };
  },
};
</script>

<style scoped>
.form-dialog {
  max-width: 500px;
}

.form-card {
  width: 100%;
  max-width: 500px;
}
</style>
