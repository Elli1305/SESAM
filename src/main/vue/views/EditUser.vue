<template>
    <q-page class="column justify-evenly" style="padding: 0 5em">
        <p class="row text-h3 justify-center">{{t('adminEdit.title') }}</p>

        <div class="self-center" style="margin: 2em; width: 25em">
            <q-input id="prename" v-model="user.firstName" :label="t('profile.firstname')" outlined style = "width: 500px"/>
            <q-space style="height: 1em"/>
            <q-input id="surname" v-model="user.lastName" :label="t('profile.lastname')" outlined style = "width: 500px"/>
            <q-space style="height: 1em"/>
            <q-input id="email" v-model="user.username" :label="t('profile.email')" outlined disable style = "width: 500px"/>

            <div>
                <q-space style="height: 2em"/>
            </div>
            <div style="min-width: 250px; max-width: 300px">
                <q-badge color="secondary" class="q-mb-md">
                    Rollen: {{ modelMultiple || '[]' }}
                </q-badge>

                <q-select
                    filled
                    v-model="modelMultiple"
                    multiple
                    :options="options"
                    use-chips
                    stack-label
                    :label= "t('adminEdit.changeRoles')"
                    style = "width: 500px"
                />
            </div>

        </div>
        <div class="row justify-evenly">
            <q-btn dense round flat color="negative" icon="delete" @click="deleteAlert = true" size="34px"></q-btn>
            <q-btn dense round flat color="positive" @click="" icon="save" size="34px"></q-btn>
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

        let roles= ref([])

        let adminRole = user.roles.some(r => r.role === 'ADMINISTRATOR' && r.granted)
        let editorRole = user.roles.some(r => r.role === 'EDITOR' && r.granted)
        let issuerRole = user.roles.some(r => r.role === 'ISSUER' && r.granted)

        if(adminRole) {
            roles.value.push('Admin');
        }
        if(editorRole) {
            roles.value.push('Editor');
        }
        if(issuerRole) {
            roles.value.push('Herausgeber');
        }

        return {
            user: user,
            deleteAlert: ref(false),
            t,
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