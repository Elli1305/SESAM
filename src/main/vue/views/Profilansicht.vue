<template>
  <q-page class="column justify-around" style="padding: 0 5em" >
    <p class="row text-h3 justify-center" >Profilansicht</p>
    <div class="" style="margin: 2em; width: 25em" >
      <q-input id="prename" outlined v-model="prename" label="Vorname" disable />
      <q-space style="height: 1em" />
      <q-input id="surname" outlined v-model="lastname" label="Name" disable />
      <q-space style="height: 1em" />
      <q-input id="email" outlined v-model="email" type="email" label="E-Mail" disable />
    </div>
    <div class="row justify-center" >
      <q-badge id="issuer" class="row justify-center" rounded color="secondary" text-color="primary" style="margin-right: 2em;  width: 10em" ><q-icon name="person" left /> Herausgeber </q-badge>
      <q-badge id="editor" class="row justify-center" rounded color="secondary" text-color="primary" style="margin-right: 2em; width: 10em"  ><q-icon name="person" left /> Bearbeiter </q-badge>
      <q-badge id="admin" class="row justify-center" rounded color="secondary" text-color="primary" style="height: 2em; width: 10em; opacity: 50%" ><q-icon name="person" left /> Admin </q-badge>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "Profilansicht"
}
</script>

<style scoped>
</style>