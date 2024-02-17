<template>
    <div class="detail-popup">
        <popup ref="popupRef" title="withdraw" :async="true" width="550px" @close="handleClose">
            <div v-if="withdrawDetails">


                <div class="grid grid-cols-2 gap-2">

                    <div class="text-opacity-50">Transaction Number (AiPay):</div>
                    <div class="font-bold">{{ withdrawDetails.aipayTransactionNumber }}</div>
                    <div class="text-opacity-50">Transaction Number (Bank):</div>
                    <div class="font-bold">{{ withdrawDetails.aipayTransactionNumber }}</div>


                    <div class="text-opacity-50">User ID:</div>
                    <div class="font-bold ">{{ withdrawDetails.payId }}</div>

                    <div class="text-opacity-50">Type/Status:</div>
                    <div class="flex">
                        {{ withdrawDetails.typeMSG }} /
                        <div class="font-bold"><span
                                :style="withdrawDetails.status === 1 ? 'color: green' : withdrawDetails.status === 2 ? 'color: orange' : 'color: red'"
                                style="border: 2px; padding: 2px;">
                                {{ withdrawDetails.statusMessage }}
                                <span class="font-light opacity-70">

                                    ({{ withdrawDetails.description }})
                                </span>
                            </span></div>
                    </div>


                    <hr class="col-span-2">

                    <div class="text-opacity-50">Iban:</div>
                    <div class="font-bold">{{ withdrawDetails.iban }}</div>

                    <div class="text-opacity-50">Bank Name:</div>
                    <div class="font-bold">{{ withdrawDetails.bankName }}</div>

                    <div class="text-opacity-50">Phone Number:</div>
                    <div class="font-bold">{{ withdrawDetails.phoneNumber }}</div>

                    <div class="text-opacity-50">Amount:</div>
                    <div class="font-bold">{{ withdrawDetails.amount }}</div>

                    <div class="text-opacity-50">Commission:</div>
                    <div class="font-bold">{{ withdrawDetails.commission }} ({{ withdrawDetails.channelCommission }} +
                        {{ withdrawDetails.payCommission }} )</div>

                    <div class="text-opacity-50">Real Amount:</div>
                    <div class="font-bold">{{ withdrawDetails.realAmount }}</div>


                    <div class="col-span-2">
                        <hr class="my-4">
                    </div>

                    <div class="text-opacity-50">Create Time:</div>
                    <div class="font-bold">{{ withdrawDetails.createTime }}</div>

                    <div class="text-opacity-50">Complete Time:</div>
                    <div class="font-bold">{{ withdrawDetails.completeTime }}</div>
                </div>





            </div>
        </popup>
    </div>
</template>

<script lang="ts" setup>
import { withdrawDetail } from '@/api/withdraw'
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'


const popupRef = shallowRef<InstanceType<typeof Popup>>()
const withdrawDetails = ref(null)

const open = () => {
    popupRef.value?.open()
}

const getDetail = async (uuid: string) => {
    try {

        const data = await withdrawDetail({
            detailId: uuid
        })
        console.log(data)
        withdrawDetails.value = data
    } catch (error) {
        console.error('something went wrong:', error)
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
