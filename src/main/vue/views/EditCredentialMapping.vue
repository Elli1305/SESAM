<template>
  <q-page class="items-center justify-center" style="display: flex">
    <div class="q-gutter-y-md column" style="max-width: 80%; min-width: 20em; display: flex">
      <div>
        <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Credentialmapping bearbeiten</h1>
      </div>
      <div >
        <q-input outlined v-model="text" dense label="Kategorie">
          <template v-slot:append>
            <q-icon name="edit" />
          </template>
        </q-input>
      <div class="q-pa-md" style="float: right">
        <q-list bordered separator>
          <q-item clickable v-ripple>
            <q-item-section>Single line item</q-item-section>
          </q-item>

          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label>Item with caption</q-item-label>
              <q-item-label caption>Caption</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label overline>OVERLINE</q-item-label>
              <q-item-label>Item with caption</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
      <div class="q-pa-md" style="float: left">
        <q-list bordered separator>
          <q-item clickable v-ripple>
            <q-item-section>Single line item</q-item-section>
          </q-item>

          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label>Item with caption</q-item-label>
              <q-item-label caption>Caption</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label overline>OVERLINE</q-item-label>
              <q-item-label>Item with caption</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "EditCredentialMapping"
}
</script>

<style scoped>

</style>