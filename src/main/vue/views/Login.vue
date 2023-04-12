<template>
  <q-page>
      <div style="padding-top: 1em; display: flex; justify-content: center">
          <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em">
              <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Login</h1>
              <q-input outlined v-model="eMail" label="E-Mail" :dense="dense" />
              <q-input outlined v-model="password" label="Passwort" :dense="dense" />
              <p style="font-size: 1em">Passwort vergessen:
                  <a href="">Passwort zurücksetzen</a>
              </p>
              <q-btn color="primary" label="Login" />
              <p style="font-size: 1em">Nicht registriert:
                  <a href="">Zur Registrierung</a>
              </p>
          </div>
      </div>
  </q-page>
</template>

<script>
export default {
    name: "Login"
}
</script>

<style scoped>
</style>