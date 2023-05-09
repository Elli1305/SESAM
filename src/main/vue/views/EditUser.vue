<template>
    <q-page class="column justify-evenly" style="padding: 0 5em">
        <p class="row text-h3 justify-center">{{ t('adminEdit.title') }}</p>

        <div class="self-center" style="margin: 2em; width: 25em">
            <q-input id="prename" v-model="user.firstName" :label="t('profile.firstname')" outlined/>
            <q-space style="height: 1em"/>
            <q-input id="surname" v-model="user.lastName" :label="t('profile.lastname')" outlined/>
            <q-space style="height: 1em"/>
            <q-input id="email" v-model="user.username" :label="t('profile.email')" outlined disable/>
        </div>
        <div class="row justify-center">
            <q-badge class="row justify-center" rounded color="secondary" text-color="primary"
                     :style="{marginRight: '2em', height: '2em', width: '10em', opacity: adminopacity}">
                <q-icon name="person" left/>
                {{ t('profile.admin') }}
            </q-badge>
            <q-badge class="row justify-center" rounded color="secondary" text-color="primary"
                     :style="{marginRight: '2em', height: '2em', width: '10em', opacity: editorOpacity}">
                <q-icon name="person" left/>
                {{ t('profile.editor') }}
            </q-badge>
            <q-badge class="row justify-center" rounded color="secondary" text-color="primary"
                     :style="{height: '2em', width: '10em', opacity: issueropacity}">
                <q-icon name="person" left/>
                {{ t('profile.issuer') }}
            </q-badge>
        </div>

        <div class="row justify-center">
            <p class="row text justify-center">{{ t('adminEdit.changeRoles') }}</p>
            <q-option-group
                    :options="options"
                    type="checkbox"
                    v-model="group"
                    size="25px"
                    inline
            />
        </div>
        <div class="row justify-evenly">
            <q-btn dense round flat color="negative" icon="delete" @click="deleteAlert = true" size="24px"></q-btn>
            <q-btn dense round flat color="positive" @click="" icon="save" size="24px"></q-btn>
        </div>

    </q-page>

    <q-dialog
            v-model="deleteAlert"
    >
        <q-card style="width: 400px">
            <q-card-section>
                <div class="text-h6">{{ t('adminEdit.attention') }}</div>
            </q-card-section>

            <q-card-section class="q-pt-none">
                {{ t('adminEdit.question') }}
            </q-card-section>

            <q-card-actions align="right" class="bg-white text-teal">
                <q-btn flat :label="t('adminEdit.back')" v-close-popup/>
                <q-btn flat :label="t('adminEdit.delete')" @click=""/>
            </q-card-actions>
        </q-card>
    </q-dialog>

</template>

<script>

import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import {useUserStore} from "@/main/vue/stores/users";

export default {
    name: "EditUser",
    setup() {
    },

    props() {
        this.$refs.admin.$props.color = "info"
    },
    data() {
        const userStore = useUserStore()
        const user = userStore.user
        const {t} = useI18n()

        const group = ref([])

        let adminOpacity = user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted) ? "100%" : "50%"
        let editorOpacity = user.roles.some(r => r.role === 'EDITOR' && r.granted) ? "100%" : "50%"
        let issuerOpacity = user.roles.some(r => r.role === 'ISSUER' && r.granted) ? "100%" : "50%"

        return {
            user: user,
            deleteAlert: ref(false),
            adminopacity: adminOpacity,
            editorOpacity: editorOpacity,
            issueropacity: issuerOpacity,
            t,
            group,
            options: [
                {label: 'Admin', value: 'ADMINISTRATOR'},
                {label: 'Bearbeiter', value: 'EDITOR'},
                {label: 'Herausgeber', value: 'ISSUER'}
            ]
        }
    }
}
</script>

<style scoped>

</style>