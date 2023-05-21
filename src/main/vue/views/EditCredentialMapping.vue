<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="display: flex">
      <div style="margin-bottom: 1em">
        <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Credentialmapping bearbeiten</h1>
      </div>
      <div style="margin-bottom: 2em; float: right; width: 120%" >
        <q-input outlined v-model="text" dense label="Kategorie" style="width: 20em">
          <template v-slot:append>
            <q-icon name="edit" />
          </template>
        </q-input>
        <div style="float: right; width: 20em; padding-top: 2em">
        <q-select
            filled
            v-model="model"
            multiple
            label="Interne Credentials"
            emit-value
            :options="options"
        ></q-select>
        </div>
        <div style="float: left; width: 20em; padding-top: 2em">
        <q-select
            filled
            v-model="model2"
            multiple
            label="Externe Credentials"
            emit-value
            :options="options2"
        ></q-select>
          <q-btn dense flat color="grey" label="New External Credential" :disable="loading" icon="add" rounded @click="show_dialog = true" no-caps/>
          <div class="q-pa-sm q-gutter-sm">
            <q-dialog v-model="show_dialog">
              <q-card>
                <q-card-section>
                  <div class="text-h6">Add new external credential</div>
                </q-card-section>

                <q-card-section>
                  <div class="row">
                    <q-input v-model="editedItem.name" label="Name des Externen Credentials"></q-input>
                    <q-input v-model="editedItem.definition" label="Credential-Definition-Id"></q-input>
                  </div>
                </q-card-section>

                <q-card-actions align="right">
                  <q-btn flat label="OK" color="primary" v-close-popup @click="addExternal" ></q-btn>
                </q-card-actions>
              </q-card>
            </q-dialog>
          </div>
          <q-btn dense flat color="grey" label="Delete External Credential" :disable="loading" icon="delete" rounded @click="deleteExternal" no-caps/>
        </div>
      </div>
      <q-btn dense flat color="positive" @click="saveEditedCategory()" icon="save" size="2em"></q-btn>
    </div>
  </q-page>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "@/main/vue/stores/users";
import {useQuasar} from "quasar";
import {useI18n} from "vue-i18n";
import router from "@/main/vue/router";

export default {
  name: "EditCredentialMapping",
  setup() {


    return {
      text: ref(''),
      model: ref(''),
      model2: ref(''),
      options: [
        'DRLG', 'Johanniter'
      ],
      options2:['Telekom', 'DRK'],
      editedItem: {
        name:'',
        defintion:''
      }
    }
  }
}
</script>

<style scoped>

</style>