<template>
  <q-page class="column justify-evenly items-center" style="padding: 2em 5em">
    <p class="row text-h3 justify-center">{{ t('admin.corporateName.title') }}</p>
    <div class="column justify-around" style="width: 80vw; height: 50vh">
      <q-input class="self-center" style="width: 40vw" outlined v-model="corporateName" :label="t('admin.corporateName.name')"/>
      <div class="row justify-evenly no-wrap">
        <q-btn @click="confirmReset = true" round icon="restart_alt" color="negative" text-color="positive"
               style="width: 4em; height: 4em"/>
        <q-btn @click="confirmSave = true" round icon="save" color="positive" text-color="negative"
               style="width: 4em; height: 4em"/>
      </div>
    </div>

    <q-dialog v-model="confirmReset" persistent>
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('admin.corporateName.reset.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{ t('admin.corporateName.reset.message') }}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.cancel')" color="primary" v-close-popup/>
          <q-btn @click="reset" flat :label="t('admin.corporateName.reset.reset')" color="primary" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="confirmSave" persistent>
      <q-card style="background-color: var(--bg-color); color: var(--text-color)">
        <q-card-section>
          <div class="text-h6">{{ t('admin.corporateName.save.title') }}</div>
        </q-card-section>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">{{ t('admin.corporateName.save.message') }}</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.cancel')" color="primary" v-close-popup/>
          <q-btn @click="save" flat :label="t('common.save')" color="primary" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>

  </q-page>

</template>

<script>
import {ref} from 'vue'
import {useI18n} from "vue-i18n"
import corpdesign from "@/main/vue/api/corpdesign";
import {useAppStore} from "@/main/vue/stores/app";

export default {
  name: "CorporateName",
  setup() {
    return {
      confirmReset: ref(false),
      confirmSave: ref(false)
    }
  },
  data() {
    const {t} = useI18n()
    const appStore = useAppStore()
    const corporateName = ref('')
    corpdesign.getColors('LIGHT').then(colors => {
      corporateName.value = colors.data.corporateName
    })

    function reset() {
      corpdesign.resetName()
      location.reload()
    }

    function save() {
      corpdesign.saveName(corporateName.value)
      location.reload()
    }

    return {
      t,
      reset,
      save,
      corporateName
    }
  }
}
</script>

<style scoped>

</style>