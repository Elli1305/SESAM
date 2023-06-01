<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em" >
    <p class="row text-h3 justify-center">{{t("adminCurrentUser.headline")}}</p>
    <div class="row justify-center">
        <q-table
            style="width: 75vw; height: 25em"
            :rows-per-page-options="[0]"
            :rows="rows"
            :columns="columns"
            row-key="username"
            :separator="'cell'"
            :no-data-label="t('common.noData')"
            :no-results-label="t('common.noResults')"
            :pagination-label="getPaginationLabel"
            :filter="filter"
            :filter-method="customFilter">
          <template v-slot:body-cell-actions="props">
              <q-td :props="props">
                  <q-btn dense round flat color="grey" @click="" icon="edit"></q-btn>
              </q-td>
          </template>
          <template v-slot:top-left>
              <div class="col-9">
                  <q-toggle v-model="filter.filterToggle.admin" val="ADMINISTRATOR" :label="t( 'adminCurrentUser.showAdmin')"/>
                  <q-toggle v-model="filter.filterToggle.editor" val="EDITOR" :label="t( 'adminCurrentUser.showEditor')" />
                  <q-toggle v-model="filter.filterToggle.issuer" val="ISSUER" :label="t( 'adminCurrentUser.showIssuer')" />
              </div>
          </template>
          <template v-slot:top-right>
              <q-input borderless dense debounce="250" v-model="filter.search" :placeholder="t( 'adminCurrentUser.search')" >
                  <template v-slot:append>
                      <q-icon name="search" />
                  </template>
              </q-input>
          </template>
          <template v-slot:body-cell-roles="props">
              <q-td :props="props">
                  <div v-for="role in props.value">
                      {{t( `adminCurrentUser.roles.${role.role}` )}}
                  </div>
              </q-td>
          </template>
        </q-table>
    </div>
  </q-page>
</template>

<script>

import {ref} from 'vue'

import {useI18n} from "vue-i18n";
import axios from "axios";


const rows = ref([]);

rows.value = []

export default {
    name: "CurrentUserList",

    setup () {
        axios.get('/api/user')
            .then(res => { rows.value = res.data

            })

        const { t } = useI18n();

        const columns = [
            {
                name: 'lastName',
                required: true,
                label: t('adminCurrentUser.lastname'),
                align: 'center',
                field: row => row.lastName,
                format: val => `${val}`,
                sortable: true
            },
            { name: 'firstName', align: 'center', label : t('adminCurrentUser.prename') , field: 'firstName', sortable: true },
            { name: 'username', align: 'center',label: t('adminCurrentUser.email') , field: 'username', sortable: true },
            { name: 'roles', align: 'center',label: t('adminCurrentUser.role'), field: 'roles' },
            { name: 'actions', label: t('adminCurrentUser.edit'), style: "width: 40px", align: 'center' }
        ]

        return {
            filter: ref({
                filterToggle: {
                    admin: true,
                    editor: true,
                    issuer: true
                },
                search: ''
            }),
            columns,
            rows,
            t
        }
    },
    methods: {
        customFilter(rows, terms){
            // rows contain the entire data
            // terms contains whatever you have as filter

            console.log(terms,rows)

            const lowerSearch = terms.search ? terms.search.toLowerCase() : ""

          return rows.filter(
                (row) => {

                  let ans

                  //Gather toggle conditions
                  let c1 = terms.filterToggle.admin && row.roles.filter(r => r.granted).map(r => r.role).includes("ADMINISTRATOR")
                  let c2 = terms.filterToggle.editor && row.roles.filter(r => r.granted).map(r => r.role).includes("EDITOR")
                  let c3 = terms.filterToggle.issuer && row.roles.filter(r => r.granted).map(r => r.role).includes("ISSUER")
                  let c4 = false
                  if (row.roles.filter(r => r.granted).length === 0) {
                    c4 = true;
                  }

                  //Gather search condition

                  //Assume true in case there is no search
                  let s1 = true

                  //If search term exists, convert to lower case and see which rows contain it
                  if (lowerSearch !== "") {
                    s1 = false
                    //Get the values
                    let s1_values = Object.values(row)
                    //Convert to lowercase
                    let s1_lower = s1_values.map(x => x.toString().toLowerCase())

                    for (let val = 0; val < s1_lower.length; val++) {
                      if (s1_lower[val].includes(lowerSearch)) {
                        s1 = true
                        break
                      }
                    }
                  }
                  //assume row doesn't match
                  ans = false
                  //check if any of the conditions match
                  if ((c1 && s1) || (c2 && s1) || (c3 && s1)) {
                    ans = true
                  } else {
                    if ((!(terms.filterToggle.admin || terms.filterToggle.editor || terms.filterToggle.issuer)) && c4) {
                      ans = true
                    }
                  }

                  return ans
                })
        },
      getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
        return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
      }
    },

}

</script>

<style scoped>

</style>