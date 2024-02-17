<template>
    <div class="edit-popup">
        <popup ref="popupRef" title="Transfer Detail" :async="true" width="550px" @close="handleClose">
            <form @submit.prevent="saveChanges">
                <div class="grid grid-cols-2 gap-4">
                    <div class="mb-4">
                        <label for="payCommission" class="block text-gray-700 text-sm font-bold">Pay Commission:</label>
                    </div>
                    <div class="mb-4">
                        <input v-model="currencyDetailData.payCommission" type="text" id="payCommission"
                            name="payCommission" class="form-input rounded-md shadow-sm border p-2">
                    </div>
                    <div class="mb-4">
                        <label for="unverifiedUserFee" class="block text-gray-700 text-sm font-bold">Unverified User
                            Fee:</label>

                    </div>
                    <div class="mb-4">

                        <input v-model="currencyDetailData.unverifiedUserFee" type="text" id="unverifiedUserFee"
                            name="unverifiedUserFee" class="form-input rounded-md shadow-sm border p-2">
                    </div>
                    <div class="mb-4">
                        <label for="verifiedUserFee" class="block text-gray-700 text-sm font-bold">Verified User
                            Fee:</label>

                    </div>
                    <div class="mb-4">

                        <input v-model="currencyDetailData.verifiedUserFee" type="text" id="verifiedUserFee"
                            name="verifiedUserFee" class="form-input rounded-md shadow-sm border p-2">
                    </div>
                </div>
                <div class="mt-6 flex justify-end" v-if="!isPasswordShow">
                    <el-button @click="showPassword"
                        class="bg-blue-500  hover:bg-blue-600 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline-blue active:bg-blue-800">
                        Save Changes
                    </el-button>
                </div>
            </form>
            <div v-if="isPasswordShow">

                <div :class="{ 'opacity-100': isPasswordShow, 'opacity-0': !isPasswordShow }"
                    class="transition-opacity duration-500">
                    <div class="text-2xl font-semibold flex justify-center items-center m-3">
                        Please enter the password
                    </div>
                    <el-card class="mt-4">
                        <el-input type="password" v-model="password"></el-input>
                        <el-button type="primary" class="w-full my-4" @click="saveChanges"> accept the changes</el-button>
                    </el-card>
                </div>
            </div>
        </popup>
    </div>
</template>
  
<script lang="ts" setup>
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'
import { configureTransfer, configureTransferUpdate } from '@/api/configure';

const emit = defineEmits(['success', 'close'])

const popupRef = shallowRef<InstanceType<typeof Popup>>()
const currencyDetailData = ref({})
const password = ref('');
const isPasswordShow = ref(false);


const showPassword = () => {
    isPasswordShow.value = !isPasswordShow.value;
    console.log("selams");

};


const open = () => {
    popupRef.value?.open()
}

const getDetail = async (currencyId: string) => {
    try {
        const data = await configureTransfer({
            currencyID: currencyId.currencyId
        })
        console.log(data);
        Object.assign(currencyDetailData.value, data);
    } catch (error) {
        console.error('Detay verisi alınamadı:', error)
    }
}

const saveChanges = async () => {
    try {
        await configureTransferUpdate({ ...currencyDetailData.value, editUuid: currencyDetailData.value.uuid, password: password.value});
        emit('success');
        handleClose()
    } catch (error) {
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
  