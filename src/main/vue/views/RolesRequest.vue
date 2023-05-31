<template>
  <div class="q-pa-md">

    <div class="q-mb-xl">
      <h1 style="font-size: 3em; text-align: center; margin-bottom: -0.5em">{{ t("adminRolesRequest.headline") }}</h1>
    </div>
    <div class="items-center justify-center" style="display: flex">
      <div class="center" style="max-width: 80em; min-width: 60em">
        <q-table
            :rows="rows"
            :columns="columns"
            row-key="username"
            :separator="'cell'"
            :filter="filter"
            :filter-method="customFilter"
        >

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
            <div class="col-9">
              <q-toggle v-model="filter.filterToggle.admin" val="ADMINISTRATOR"
                        :label="t( 'adminCurrentUser.showAdmin')"/>
              <q-toggle v-model="filter.filterToggle.editor" val="EDITOR" :label="t( 'adminCurrentUser.showEditor')"/>
              <q-toggle v-model="filter.filterToggle.issuer" val="ISSUER" :label="t( 'adminCurrentUser.showIssuer')"/>
            </div>
          </template>

          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="filter.search"
                     :placeholder="t( 'adminCurrentUser.search')">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </template>

          <template v-slot:body-cell-roles="props">
            <q-td :props="props">
              <div class="q-gutter-xs" v-for="role in props.value">
                <q-chip v-model:selected="role.selected" color="secondary" text-color="primary"
                        :style="{padding: '0.4em 1em 0.4em 0.5em', fontSize: '1em'}"
                        icon="highlight_off">
                  {{ t(`adminCurrentUser.roles.${role.role}`) }}
                </q-chip>
              </div>
            </q-td>
          </template>
        </q-table>
      </div>
    </div>
  </div>
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
                name: t('adminRolesRequest.lastname'),
        required: true,
        label: 'Name',
        align: 'center',
        field: row => row.lastName,
        format: val => `${val}`,
        sortable: true
      },
      {name: 'firstName', align: 'center', label: t('adminRolesRequest.prename'), field: 'firstName', sortable: true},
      {name: 'username', align: 'center', label: t('adminRolesRequest.email'), field: 'username', sortable: true},
      {name: 'roles', align: 'center', label: t('adminRolesRequest.role'), field: 'roles'},
      {
        name: 'actions',
        label: t('adminRolesRequest.save'),
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

      const filteredRows = rows.filter(
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
      return filteredRows
    }
  },

}

</script>

<style scoped>

</style>