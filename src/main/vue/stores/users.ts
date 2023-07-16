import {defineStore} from 'pinia'
import {Ref, ref, UnwrapRef} from 'vue'
import api from '../api'
import {AttainableRole, UserCount} from "@/main/vue/entity/createUser"
import axios from 'axios';
import {User} from "@/main/vue/entity/loginResponse";
import {LoginData} from "@/main/vue/entity/loginData"
import {Issuer} from "@/main/vue/entity/issuer";

export const useUserStore = defineStore('users', () => {
    const authenticated: Ref<boolean> = ref(false)
    const validPassword: Ref<RegExpMatchArray | null> = ref(null)
    const validEmail: Ref<RegExpMatchArray | null> = ref(null)
    const validName: Ref<RegExpMatchArray | null> = ref(null)
    const user: Ref<User | null> = ref(null)
    const comparePassword: Ref<boolean> = ref(false)
    const editUser: Ref<User | null> = ref(null)
    const issuers: Ref<Issuer [] | null> = ref(null)
    const notGrantedCount: Ref<UserCount | null> = ref(null)


    if (sessionStorage.getItem("users")) {
        const state = JSON.parse((sessionStorage.getItem("users") || ''));
        authenticated.value = state.authenticated;
        user.value = state.user;
    }

    if (sessionStorage.getItem("token")) {
        axios.defaults.headers['Authorization'] = 'Bearer ' + sessionStorage.getItem("token")
    }

    function signUp(email: string, password: string, firstName: string, lastName: string, roles: AttainableRole[]): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            api.auth.signUp({
                firstName: firstName,
                lastName: lastName,
                password: password,
                email: email,
                requestedRoles: roles,
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    function validatePassword(password: string, passwordRepeat: string) {
        const passwordRegEx = /^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*+_-])[a-zA-Z0-9!@#$%^&+_*-]{8,120}$/;
        validPassword.value = password.match(passwordRegEx)
        comparePassword.value = passwordRepeat === password
    }

    function authenticate(token?: string) {
        if (token) {
            authenticated.value = true
            sessionStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = 'Bearer ' + token
        } else {
            authenticated.value = false
        }
    }

    function validateName(name: string) {
        const nameRegex = /^[a-zA-ZÆÐƎƏƐƔĲŊŒẞÞǷȜæðǝəɛɣĳŋœĸſßþƿȝĄƁÇĐƊĘĦĮƘŁØƠŞȘŢȚŦŲƯY̨Ƴąɓçđɗęħįƙłøơşșţțŧųưy̨ƴÁÀÂÄǍĂĀÃÅǺĄÆǼǢƁĆĊĈČÇĎḌĐƊÐÉÈĖÊËĚĔĒĘẸƎƏƐĠĜǦĞĢƔáàâäǎăāãåǻąæǽǣɓćċĉčçďḍđɗðéèėêëěĕēęẹǝəɛġĝǧğģɣĤḤĦIÍÌİÎÏǏĬĪĨĮỊĲĴĶƘĹĻŁĽĿʼNŃN̈ŇÑŅŊÓÒÔÖǑŎŌÕŐỌØǾƠŒĥḥħıíìiîïǐĭīĩįịĳĵķƙĸĺļłľŀŉńn̈ňñņŋóòôöǒŏōõőọøǿơœŔŘŖŚŜŠŞȘṢẞŤŢṬŦÞÚÙÛÜǓŬŪŨŰŮŲỤƯẂẀŴẄǷÝỲŶŸȲỸƳŹŻŽẒŕřŗſśŝšşșṣßťţṭŧþúùûüǔŭūũűůųụưẃẁŵẅƿýỳŷÿȳỹƴźżžẓ\s-,.\']+$/
        validName.value = name.match(nameRegex)
    }

    function validateEmail(email: string) {
        const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        validEmail.value = email.match(emailRegex)
    }

    function requestToken(credentials: LoginData): Promise<void> {
        return new Promise((resolve, reject) => {
            api.auth.login(credentials).then((res) => {
                authenticate(res.data.token)
                user.value = res.data.user
                resolve()
            }).catch((error) => {
                authenticate()
                reject(error)
            })
        })
    }

    function logout() {
        if (sessionStorage.getItem('token')) {
            sessionStorage.removeItem('token')
            authenticated.value = false;
        }
    }

    function resetPassword(email: string) {
        return new Promise<void>((resolve, reject) => {
            api.auth.resetPassword({
                email: email,
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    function changePassword(password: string, token: string) {
        return new Promise<void>((resolve, reject) => {
            api.auth.changePassword({
                password: password,
                token: token,
            }).then(_ => resolve())
                .catch(reject);
        });
    }

    function getEditUser(mail: string) {
        return new Promise<void>((resolve, reject) => {
            api.auth.getEditUser(mail).then((res) => {

                editUser.value = res.data
                console.log(editUser.value)
                resolve()
            }).catch((error) => {

                reject(error)
            })
        })
    }

    function saveEdits(prename: string, lastname: string, mail: string, roles: []) {
        return new Promise<void>((resolve, reject) => {
            api.auth.editUser({
                firstName: prename,
                lastName: lastname,
                username: mail,
                roles: roles,
            }).then(_ => {
                resolve()
            })
                .catch(reject);
        });
    }

    function deleteUser(mail: string) {
        return new Promise<void>((resolve, reject) => {
            api.auth.deleteUser(mail).then(_ => resolve())
                .catch(reject);
        });
    }


    function updateIssuer(id: number, credential: string[], room: string) {
        return new Promise<void>((resolve, reject) => {
            api.auth.updateIssuer({
                issuerId: id,
                credentials: credential,
                room: room,
            })
                .then(() => {
                    resolve();
                })
                .catch(reject);
        });
    }

    function getIssuers() {
        return new Promise((resolve, reject) => {
            api.auth.getIssuer().then((response) => {
                issuers.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

    function getNotGrantedCount() {
        return new Promise((resolve, reject) => {
            api.auth.getNotGrantedCount().then((response) => {
                notGrantedCount.value = response.data
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }


    // @ts-ignore
    // @ts-ignore
    return {
        user,
        authenticated,
        signUp,
        authenticate,
        validatePassword,
        validPassword,
        comparePassword,
        validateEmail,
        validEmail,
        validName,
        validateName,
        logout,
        requestToken,
        resetPassword,
        changePassword,
        getEditUser,
        editUser,
        saveEdits,
        deleteUser,
        updateIssuer,
        getIssuers,
        issuers,
        getNotGrantedCount,
        notGrantedCount
    }
});