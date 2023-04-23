 <template>
    <q-page class="items-center justify-center" style="display: flex">
      <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
        <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Passwort zurücksetzen</h1>
        <q-input v-model="email" outlined type="email" label="Email" />
        <q-btn @click="resetPassword" color="primary" label="Passwort zurücksetzen"/>
      </div>
    </q-page>
 </template>

<script>
import { ref } from 'vue'
import {useUserStore} from "../stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
export default {
  name: "PasswordReset",
  setup () {
    const $q = useQuasar()
    const userStore = useUserStore()
    const email = ref('')
    async function resetPassword() {
      userStore.validateEmail(email.value);
      if(!userStore.validEmail){
        $q.notify({
          type: 'negative',
          message: 'Email versenden fehlgeschlagen',
          caption: 'E-Mail erfüllt nicht die Kriterien',
          classes: "loginNotify"
        })
      } else {
        $q.notify({
          type: 'positive',
          message: 'E-Mail ist valide',
          classes: "loginNotify"
        })
      }
      await userStore.resetPassword({eMail: email.value})
          .catch((error) => {
            console.log(error)
            if(error.response.status === 403) {
              $q.notify({
                type: 'negative',
                message: 'Email versenden fehlgeschlagen',
                caption: 'Falsche Mail',
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            } else if(error.response.status === 500) {
              $q.notify({
                type: 'negative',
                message: 'Email versenden fehlgeschlagen',
                caption: 'Der Server konnte die Anfrage nicht verarbeiten',
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            } else {
              $q.notify({
                type: 'negative',
                message: 'Email versenden fehlgeschlagen',
                caption: 'Etwas ist schiefgelaufen',
                position: "top",
                timeout: 3000,
                classes: "loginNotify"
              })
            }
          })
    }
    return {email, resetPassword}
  }
}
</script>

<style scoped>

</style>