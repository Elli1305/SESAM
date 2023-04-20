<template>
    <div class="q-pa-md">

        <div class="q-gutter-md q-mt-xs row">
            <q-select
                v-model="model"
                use-input
                input-debounce="0"
                label="Filter"
                :options="options"
                @filter="filterFn"
                style="width: 250px"
                behavior="menu"
                outlined
            >
                <template v-slot:no-option>
                    <q-item>
                        <q-item-section class="text-grey">
                            No results
                        </q-item-section>
                    </q-item>
                </template>
            </q-select>

            <q-input v-model="search" type="search" style="width: 250px" outlined label="Suche">
                <template v-slot:append>
                    <q-icon name="search" />
                </template>
            </q-input>


            <div>
                <q-btn class="q-mr-xs" size="12px" round icon="create" />
                <q-tooltip :offset="[0, 8]">Konten bearbeiten</q-tooltip>
            </div>


        </div>


        <div class="q-mb-xl">
            <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">Aktuelle Nutzer</h1>
        </div>


        <div class="items-center justify-center" style="display: flex">
            <div class="center" style="max-width: 80em; min-width: 60em">
                <q-table
                        :rows="rows"
                        :columns="columns"
                        row-key="name"
                        :separator="'cell'"


                />
            </div>
        </div>

    </div>
</template>

<script>

import { ref } from 'vue'

const columns = [
    {
        name: 'name',
        required: true,
        label: 'Name',
        align: 'center',
        field: row => row.name,
        sortable: true
    },
    { name: 'prename', align: 'center', label: 'Vorname', field: 'prename', sortable: true },
    { name: 'email', align: 'center',label: 'E-mail', field: 'email', sortable: true },
    { name: 'roles', align: 'center',label: 'Rolle(n)', field: 'roles' }
]

const rows = [
    {
        name: 'Frozen Yogurt',
        prename: 159,
        email: 6.0,
        roles: 24
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
        return {
            search: ref(''),
            columns,
            rows
        }
    }
}

</script>

<style scoped>

</style>