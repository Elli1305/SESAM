import axios from 'axios';

export const getImprintContent = async () => {
    try {
        const response = await axios.get('/api/imprint');
        return response.data;
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const postImprintContent = async (content: string) => {
    try {
        const response = await axios.post('/api/imprint', { content });
        return response.data;
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const deleteImprintContent = async () => {
    try {
        const response = await axios.delete('/api/imprint');
        return response.data;
    } catch (error) {
        console.error(error);
        return null;
    }

};

export async function getLatestImprint() {
    try {
        const response = await axios.get('/api/imprint/latest');
        return response.data;
    } catch (error) {
        console.error(error);
        throw new Error('Error fetching latest imprint content');
    }
}
