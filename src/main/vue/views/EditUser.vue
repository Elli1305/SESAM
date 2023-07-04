<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em" v-if="user!=null">
    <p class="row text-h3 justify-center">{{t('admin.currentUsers.editUser.title') }}</p>
    <div class="column justify-evenly self-center no-wrap" style="width: 80vw; height: 50vh">
      <div class="column q-gutter-md self-center" style="width: 25em">
        <q-input id="prename" v-model="user.firstName" :label="t('profile.firstName')" outlined/>
        <q-input id="lastname" v-model="user.lastName" :label="t('profile.lastName')" outlined/>
        <q-input id="email" v-model="user.username" :label="t('profile.email')" outlined disable/>
        <q-select
            filled
            v-model="modelMultiple"
            multiple
            :options="options"
            use-chips
            stack-label
            :label= "t('admin.currentUsers.editUser.changeRoles')">
          <template v-slot:selected-item="scope">
            <q-chip
                class="q-pa-sm"
                style="line-height: 1"
                :label="scope.opt.toString()"
                :tabindex="scope.tabindex"
                dense
                removable
                icon-remove="clear"
                @remove="scope.removeAtIndex(scope.index)"/>
          </template>
        </q-select>
      </div>
      <div class="row justify-around">
          <q-btn round color="negative" text-color="positive" icon="delete" @click="deleteAlert = true" style="width: 4em; height: 4em"/>
          <q-btn round color="positive" text-color="negative" @click="saveEditedUser()" icon="save" style="width: 4em; height: 4em"/>
      </div>
    </div>
  </q-page>

  <q-dialog v-model="deleteAlert">
      <q-card>
          <q-card-section>
              <div class="text-h6">{{ t('admin.currentUsers.editUser.deleteUser') }}</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
              {{ t('admin.currentUsers.editUser.question') }}
          </q-card-section>
          <q-card-actions align="right" class="bg-white text-primary">
              <q-btn flat :label="t('common.cancel')" v-close-popup/>
              <q-btn flat :label="t('common.delete')" @click="deleteUser()"/>
          </q-card-actions>
      </q-card>
  </q-dialog>

</template>

<script>

import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users";
import router from "@/main/vue/router";
import {useQuasar} from "quasar";


let test = ref('')

export default {
  name: "EditUser",

  props: ['email'],


  setup(props) {
    const userStore = useUserStore()
    const $q = useQuasar()
    const currentUser = userStore.user

    let roles = ref([])
    const {t} = useI18n()

    let user = ref(null)


    userStore.getEditUser(props.email).then(() => {
      user.value = userStore.editUser


      let adminRole = user.value.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)
      let editorRole = user.value.roles.some(r => r.role === 'EDITOR' && r.granted)
      let issuerRole = user.value.roles.some(r => r.role === 'ISSUER' && r.granted)

      if (adminRole) {
        roles.value.push('Admin');
      }
      if (editorRole) {
        roles.value.push('Bearbeiter');
      }
      if (issuerRole) {
        roles.value.push('Herausgeber');
      }

    })

    function saveEditedUser() {

      let prename;
      let lastname;
      let mail;
      prename = user.value.firstName
      lastname = user.value.lastName
      mail = user.value.username

      userStore.saveEdits(prename, lastname, mail, roles.value.map(r => {
        if (r === 'Admin') {
          return 'ADMINISTRATOR'
        }
        if (r === 'Bearbeiter') {
          return 'EDITOR'
        }
        if (r === 'Herausgeber') {
          return 'ISSUER'
        }
      })).then(() => {
        router.push('/currentuserlist');
      })
    }

    function deleteUser() {
      let mail = user.value.username

      if (mail === currentUser.username) {
        $q.notify({
          type: 'negative',
          message: t('admin.currentUsers.editUser.deleteOwnAccount'),
          caption: t('admin.currentUsers.editUser.otherAdmin'),
        })

      } else {
        userStore.deleteUser(mail);
        router.push('/currentuserlist');
      }
    }


    return {
      user: user,
      deleteAlert: ref(false),
      t,
      deleteUser,


      saveEditedUser,
      modelMultiple: roles,

      options: [
        'Admin', 'Bearbeiter', 'Herausgeber'
      ]
    }
  }
}
</script>

<style scoped>

</style>