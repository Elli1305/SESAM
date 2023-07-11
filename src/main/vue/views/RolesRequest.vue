<template>
  <q-page class="column justify-evenly" style="padding: 2em 5em" >
    <p class="row text-h3 justify-center">{{t("admin.requestedRoles.title")}}</p>
    <div class="row justify-center">
      <q-table
          style="width: 80vw; height: 50vh; background-color: var(--bg-color); color: var(--text-color)"
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
        <template>
          <q-tr :props="props" @click="onRowClick(props.row)">
            <q-td key="lastName" :props="props">
              <q-badge color="green">
                {{ props.row.lastname }}
              </q-badge>
            </q-td>
          </q-tr>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn outline round flat color="primary" @click="updateRoles(Object.values(props))" icon="done"></q-btn>
          </q-td>
        </template>
        <template v-slot:top-left>
            <q-toggle v-model="filter.filterToggle.admin" val="ADMINISTRATOR"
                      :label="t('profile.administrators')" size="2.5em"/>
            <q-toggle v-model="filter.filterToggle.editor" val="EDITOR" :label="t('profile.editors')" size="2.5em"/>
            <q-toggle v-model="filter.filterToggle.issuer" val="ISSUER" :label="t('profile.issuers')" size="2.5em"/>
        </template>
        <template v-slot:top-right>
          <q-input outlined rounded dense debounce="300" v-model="filter.search"
                   :placeholder="t('common.search')">
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-roles="props">
          <q-td :props="props">
            <div class="column items-center justify-evenly" v-for="role in props.value">
              <q-chip v-model:selected="role.selected" color="secondary" text-color="primary"
                      style="padding: 0.4em 0.75em 0.4em 0.75em; font-size: 1em"
                      icon="not_interested" icon-selected="account_circle">
                {{ t('profile.' + role.role.toLowerCase()) }}
              </q-chip>
            </div>
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script>

import {computed, reactive, ref} from 'vue'

import {useI18n} from "vue-i18n";
import axios from "axios";
import {useUserStore} from "@/main/vue/stores/users";


const rows = ref([]);

rows.value = []


export default {
  name: "RolesRequest",
  //axios.get(url[,config]),


  setup() {
    const userStore = useUserStore()
    axios.get('/api/user')
        .then(res => {
          rows.value = res.data.map(v => ({
            ...v,
            roles: v.roles.filter(r => !r.granted).map(r => ({...r, selected: false}))
          }))

        })

    const {t} = useI18n();

    const columns = [
      {
        required: true,
        label: t('profile.lastName'),
        align: 'center',
        field: row => row.lastName,
        format: val => `${val}`,
        sortable: true
      },
      {name: 'firstName', align: 'center', label: t('profile.firstName'), field: 'firstName', sortable: true},
      {name: 'username', align: 'center', label: t('profile.email'), field: 'username', sortable: true},
      {name: 'roles', align: 'center', label: t('profile.roles'), field: 'roles'},
      {
        name: 'actions',
        label: t('common.save'),
        style: "width: 40px",
        align: 'center',
        field: row => row.roles
      }
    ]

    const roles = reactive({
      admin: false,
      editor: false,
      issuer: false
    })

    function updateRoles(test) {
      let prename = test[1].firstName;
      let lastname = test[1].lastName;
      let email = test[1].username;
      let role = test[1].roles;

      let rolesD = [];

      for (let i = 0; i < role.length; i++) {
        if (role[i].selected === true) {
          rolesD.push(role[i].role);
        }
      }


      userStore.saveEdits(prename, lastname, email, rolesD).then(() =>
          axios.get('/api/user')
              .then(res => {
                rows.value = res.data.map(v => ({
                  ...v,
                  roles: v.roles.filter(r => !r.granted).map(r => ({...r, selected: false}))
                }))

              }))
    }

    return {
      filter: ref({
        filterToggle: {
          admin: true,
          editor: true,
          issuer: true
        },
        search: '',
        roles,
        selection: computed(() => {
          return Object.keys(roles)
              .filter(type => roles[type] === true)
              .join(', ')
        })
      }),
      columns,
      rows,
      t,
      onRowClick: (row) => alert(`${row.name} clicked`),
      updateRoles,
    }
  },
  methods: {
    customFilter(rows, terms) {

      const lowerSearch = terms.search ? terms.search.toLowerCase() : ""

      return rows.filter(
          (row) => {

            let ans

            //Gather toggle conditions
            let c1 = terms.filterToggle.admin && row.roles.filter(r => !r.granted).map(r => r.role).includes("ADMINISTRATOR")
            let c2 = terms.filterToggle.editor && row.roles.filter(r => !r.granted).map(r => r.role).includes("EDITOR")
            let c3 = terms.filterToggle.issuer && row.roles.filter(r => !r.granted).map(r => r.role).includes("ISSUER")

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
            }

            return ans
          })
    },
    getPaginationLabel(firstRowIndex, endRowIndex, totalRowsNumber) {
      return firstRowIndex.toString() + "-" + endRowIndex.toString() + " / " + totalRowsNumber.toString()
    }
  }

}

</script>

<style scoped>

</style>