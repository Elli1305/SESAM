<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin" style="width: 80em; max-width: fit-content">
      <q-card-section class="q-pa-md">
        <div class="text-h6">Tür zuweisen</div>
        <div class="q-mt-md">
          <q-input filled v-model="doorName" label="Türname" stack-label
                   style="width: 250px; padding-bottom: 32px"/>
        </div>
        <div class="q-mt-md">
          <q-select
              filled
              v-model="room"
              use-input
              hide-selected
              label="Raum auswählen"
              option-label="name"
              fill-input
              input-debounce="0"
              :options="rooms"
              @filter="filterFn"
              style="width: 250px; padding-bottom: 32px"
          >
            <template v-slot:no-option>
              <q-item>
                <q-item-section class="text-grey">
                  No results
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </div>
        <div class="q-gutter-sm">
          <div>
            <q-select
                filled
                multiple
                label="Credentials"
                option-label="name"
                hint="Werbung1"
                :options="credentialStore.allCredentials"
                v-model="credentials"
                use-chips
            >
            </q-select>
          </div>
          <q-card bordered flat style="min-width: 60em">
            <q-toolbar class="bg-primary text-white shadow-2" style="margin-bottom: 1em">
              <q-toolbar-title>Konfigurationsgruppen</q-toolbar-title>
            </q-toolbar>
            <q-card-section>
              <div class="row q-gutter-sm" v-for="(select,i) in qSelects.configParts">
                <q-select
                    filled
                    multiple
                    label="Credentials für Und"
                    option-label="name"
                    hint="Werbung2"
                    :options="credentials"
                    v-model="qSelects.configParts[i].credentials"
                    use-chips
                    style="min-width: 100%"
                >
                  <template v-slot:no-option>
                    <q-item>
                      <q-item-section class="text-grey">
                        No results
                      </q-item-section>
                    </q-item>
                  </template>
                </q-select>
                <q-toolbar class="bg-primary text-white shadow-2" style="min-width: 100%">
                  <q-toolbar-title>Konfiguration</q-toolbar-title>
                </q-toolbar>
                <div class="q-gutter-sm row no-wrap" style="min-width: 100%">
                  <div style="width: 42%">
                    <q-select>

                    </q-select>
                  </div>
                  <div style="width: 16%">
                    <q-select>

                    </q-select>
                  </div>
                  <div style="width: 42%">
                    <q-input>

                    </q-input>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>

      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" label="Abbrechen" @click="onCancelClick"/>
        <q-btn color="primary" label="Speichern" @click="onOKClick"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import {ref} from "vue";
import {useCredentialStore} from "@/main/vue/stores/credential";

export default {
  props: {
    rooms: {
      required: true
    }
  },

  emits: [
    'ok', 'hide'
  ],

  methods: {
    show() {
      console.log(this.$refs)
      this.$refs.dialog.show()
    },
    hide() {
      this.$refs.dialog.hide()
    },
    onDialogHide() {
      this.$emit('hide')
    },

    onOKClick() {
      this.$emit('ok', {room: this.room, doorName: this.doorName})
      this.hide()
    },
    onCancelClick() {
      this.hide()
    }
  },
  setup(props) {
    const room = ref(null)
    const doorName = ref('')
    const roomOptions = ref(props.rooms)
    const credentialStore = useCredentialStore()
    credentialStore.getAllCredentials()
    const credentials = ref()

    const qSelects = ref({
      configParts: [{
        credentials: []
      }]
    })

    const filterFn = function (val, update, abort) {
      update(() => {
        const needle = val.toLowerCase()
        roomOptions.value = props.rooms.filter(room => room.name.toLowerCase().indexOf(needle) > -1)
      })
    }

    const commonAttributeFilter = function (credentials) {
      let arr4 = credentials.map((credential) => {
        return credential.form
      })
      return arr4.shift().filter(function() {
        return arr4.every(function(a) {
          return a.indexOf(v) !== -1;
        });
      });
    }

    return {room, credentials, filterFn, doorName, credentialStore, qSelects}
  }
}
</script>