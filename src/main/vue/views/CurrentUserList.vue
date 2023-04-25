<template>
    <div class="q-pa-md">

        <div class="q-mb-xl">
            <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{t("adminCurrentUser.headline")}}</h1>
        </div>



        <div class="items-center justify-center" style="display: flex">
            <div class="center" style="max-width: 80em; min-width: 60em">
                <q-table
                        :rows="rows"
                        :columns="columns"
                        row-key="name"
                        :separator="'cell'"
                        :filter="filter"
                >

                    <template v-slot:body-cell-actions="props">
                        <q-td :props="props">
                            <q-btn dense round flat color="grey" @click="" icon="edit"></q-btn>
                        </q-td>
                    </template>


                    <template v-slot:top-right>
                        <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                            <template v-slot:append>
                                <q-icon name="search" />
                            </template>
                        </q-input>
                    </template>

                </q-table>





            </div>
        </div>

    </div>
</template>

<script>

import { ref } from 'vue'

import {useI18n} from "vue-i18n";

const columns = [
    {
        name: 'name',
        required: true,
        label: 'Name',
        align: 'center',
        field: row => row.name,
        sortable: true
    },
    { name: 'prename', align: 'center', label : 'Vorname' , field: 'prename', sortable: true },
    { name: 'email', align: 'center',label: 'E-mail', field: 'email', sortable: true },
    { name: 'roles', align: 'center',label: 'Rolle(n)', field: 'roles' },
    { name: 'actions', label: 'Bearbeiten', style: "width: 40px", align: 'center' }
]

const rows = [
    {
        name: 'Frozen Yogurt',
        prename: 'Frozen',
        email: 'frozenyogourt@isbest.yeah',
        roles: 'Admin'
    },
    {
        name: ' Yogurt Frozen',
        prename: 'Yogurt',
        email: 'yogurt.frozen@isnice.too',
        roles: 'Herausgeber'
    },
    {
        name: 'Fren Yourt',
        prename: 159,
        email: 6.0,
        roles: 'Admin, Bearbeiter'
    },
    {
        name: 'Frozen Yogurt',
        calories: 159,
        fat: 6.0,
        carbs: 24,
        protein: 4.0,
        sodium: 87,
        calcium: '14%',
        iron: '1%'
    },
    {
        name: 'Frozen Yogurt',
        calories: 159,
        fat: 6.0,
        carbs: 24,
        protein: 4.0,
        sodium: 87,
        calcium: '14%',
        iron: '1%'
    },
    {
        name: 'Frozen Yogurt',
        calories: 159,
        fat: 6.0,
        carbs: 24,
        protein: 4.0,
        sodium: 87,
        calcium: '14%',
        iron: '1%'
    },
    {
        name: 'Frozen Yogurt',
        calories: 159,
        fat: 6.0,
        carbs: 24,
        protein: 4.0,
        sodium: 87,
        calcium: '14%',
        iron: '1%'
    }
]

export default {
    name: "CurrentUserList",
    setup () {

        const { t } = useI18n();
        return {
            filter: ref(''),
            search: ref(''),
            columns,
            rows,
            t
        }
    }
}

</script>

<style scoped>

</style>