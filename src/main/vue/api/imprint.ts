import axios from 'axios';

export const getImprintContent = async () => {
       return axios.get('api/imprint');

};

export const postImprintContent = async (content: string) => {
    return axios.post('api/imprint', {content});


};

export const deleteImprintContent = async () => {
    return axios.delete('api/imprint');


};

export const  getLatestImprint =async () => {
    return axios.get('api/imprint/latest');

}
