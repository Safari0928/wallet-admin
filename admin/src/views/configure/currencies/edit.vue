<template>
    <div class="edit-popup">
        <popup ref="popupRef" title="Currency Detail" :async="true" width="550px" @close="handleClose">

            <div class="mb-4">
                <img :src="currencyDetailData.image" alt="" class="h-9">
            </div>

            <form @submit.prevent="saveChanges">
                <div class="mb-4">
                    <label for="currencyName" class="block text-gray-700 text-sm font-bold mb-2">Currency Name:</label>
                    <input type="text" id="currencyName" v-model="currencyDetailData.currencyName"
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <div class="mb-4">
                    <label for="currencyCode" class="block text-gray-700 text-sm font-bold mb-2">Currency Code:</label>
                    <input type="text" id="currencyCode" v-model="currencyDetailData.currencyCode"
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <div class="mb-4">
                    <label for="symbol" class="block text-gray-700 text-sm font-bold mb-2">Symbol:</label>
                    <input type="text" id="symbol" v-model="currencyDetailData.symbol"
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <div class="mb-4">
                    <label for="operate" class="block text-gray-700 text-sm font-bold mb-2">Enabled:</label>
                    <input type="checkbox" id="operate" v-model="currencyDetailData.operate">
                </div>
                <!-- <div class="mb-4">
                    <label for="image" class="block text-gray-700 text-sm font-bold mb-2">Upload Image:</label>
                    <input type="file" id="image" @change="handleImageChange"
                        class="py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    <img v-if="currencyDetailData.imagePreview" :src="currencyDetailData.imagePreview" alt="Image Preview"
                        class="mt-2">
                </div> -->
                <div class="flex justify-between" v-if="!isPasswordShow">
                    <el-button type="submit" class="bg-green-500 hover:bg-green-700 font-bold py-2 px-4 rounded"
                        @click="showPassword">
                        Save
                    </el-button>
                    <el-button @click="cancelEdit" class="bg-red-500 hover:bg-red-700 font-bold py-2 px-4 rounded">
                        Cancel
                    </el-button>
                </div>
                <div v-if="isPasswordShow">

                    <div :class="{ 'opacity-100': isPasswordShow, 'opacity-0': !isPasswordShow }"
                        class="transition-opacity duration-500">
                        <div class="text-2xl font-semibold flex justify-center items-center m-3">
                            Please enter the password
                        </div>
                        <el-card class="mt-4">
                            <el-input type="password" v-model="password"></el-input>
                            <el-button type="primary" class="w-full my-4" @click="saveChanges"> accept the
                                changes</el-button>
                        </el-card>
                    </div>
                </div>
            </form>



        </popup>
    </div>
</template>
  
<script lang="ts" setup>
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'
import { configureCurrency, configureCurrencyUpdate } from '@/api/configure';

const emit = defineEmits(['success', 'close'])

const popupRef = shallowRef<InstanceType<typeof Popup>>()
const currencyDetailData = ref({})
const password = ref('');
const isPasswordShow = ref(false);


const showPassword = () => {
    isPasswordShow.value = !isPasswordShow.value;

};
const open = () => {
    popupRef.value?.open()
}

const getDetail = async (currencyId: string) => {
    try {
        const data = await configureCurrency({
            currencyID: currencyId.currencyId
        })
        currencyDetailData.value = { ...data, uuid: currencyId.currencyId, currencySymbol: data.symbol, operate: data.operate == 1 ? true : false }
        console.log(data);

    } catch (error) {
        console.error('Detay verisi alınamadı:', error)
    }
}

const handleImageChange = (event: Event) => {
    const inputElement = event.target as HTMLInputElement;
    const file = inputElement.files?.[0];

    if (file) {

        const reader = new FileReader();
        reader.onload = () => {
            currencyDetailData.value.imagePreview = reader.result as string;
        };
        reader.readAsDataURL(file);


        currencyDetailData.value.image = file;
    }
};

const saveChanges = async () => {
    console.log(currencyDetailData.value);
    currencyDetailData.value.operate = currencyDetailData.value.operate ? 1 : -1;

    try {

        await configureCurrencyUpdate({
            ...currencyDetailData.value,
            password: password.value
        });
        handleClose()
    }
    catch {
        console.error('Güncelleme başarısız:', error)

    }
}



const handleClose = () => {
    emit('close')
}

defineExpose({
    open,
    getDetail
})
</script>
  