<template>
  <q-page>
      <div style="padding: 1em; display: flex; justify-content: center">
          <div class="q-gutter-y-md column" style="max-width: 40em; min-width: 20em">
              <q-input outlined v-model="eMail" label="E-Mail" :dense="dense" />
              <q-input outlined v-model="password" label="Passwort" :dense="dense" />
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