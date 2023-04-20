 <template>
    <q-page class="items-center justify-center" style="display: flex">
      <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em; display: flex">
        <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Passwort zurücksetzen</h1>
        <div class="q-gutter-md col items-start q-mt-xs">
          <q-input v-model="email" outlined type="email" label="Email" />
        </div>
        <q-btn @click="passwortReset" color="primary" label="Passwort zurücksetzen"/>
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
    const router = useRouter()
    const userStore = useUserStore()
    const email = ref('')
    async function resetPassword() {
      userStore.validateEmail(email.value);
      if(!userStore.validEmail){
        $q.notify({
          type: 'negative',
          message: 'Registrierung fehlgeschlagen',
          caption: 'E-Mail erfüllt nicht die Kriterien'
        })
      } else {
        $q.notify({
          type: 'positive',
          message: 'E-Mail wird versendet'
        })
      }
    }
    return {
      resetPassword
    }
  }
}
</script>

<style scoped>

</style>