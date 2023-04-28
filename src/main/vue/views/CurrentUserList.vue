<template>
    <div class="q-pa-md">

        <div class="q-mb-xl">
            <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{t("adminCurrentUser.headline")}}</h1>
        </div>
        <div>
            <button v-on:click="getUserData">Aktualisieren</button>
            <div>{{userDataList}}</div>
        </div>
        <div class="items-center justify-center" style="display: flex">
            <div class="center" style="max-width: 80em; min-width: 60em">
                <q-table
                    :row-key="ids"
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
import axios from "axios";

const columns = [
    {
        name: 'lastName',
        required: true,
        label: 'Name',
        align: 'center',
        field: row => row.lastName,
        sortable: true
    },
    { name: 'firstName', align: 'center', label : 'Vorname' , field: 'firstName', sortable: true },
    { name: 'username', align: 'center',label: 'E-mail', field: 'username', sortable: true },
    { name: 'roles', align: 'center',label: 'Rolle(n)', field: 'roles' },
    { name: 'actions', label: 'Bearbeiten', style: "width: 40px", align: 'center' }
]

const rows = ref('');

rows.value = [
    {
        lastName: 'Frozen Yogurt',
        firstName: 'Frozen',
        username: 'frozenyogourt@isbest.yeah',
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
    }
]

export default {
    name: "CurrentUserList",
    //axios.get(url[,config]),


    setup () {

        axios.get('/api/user')
             .then(res => { rows.value = res.data

        })

        const { t } = useI18n();
        return {
            filter: ref(''),
            search: ref(''),
            columns,
            rows,
            t
        }
    },
    methods: {
        getUserData() {
             (this.rows = axios.get('/api/user')
                    .then(res => { rows.value = res.data
                    }));
        }
    }

}

</script>

<style scoped>

</style>