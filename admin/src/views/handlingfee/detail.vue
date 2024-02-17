<template>
    <div class="detail-popup">
        <popup ref="popupRef" title="" :async="true" width="550px" @close="handleClose">
            <div v-if="depositDetailData">
                <div class="grid grid-cols-2 gap-2">
                    <div class="col-span-2 text-center font-bold text-xl mt-4 mb-2">Payment Information</div>

                    <div class="text-opacity-50">Transaction Number:</div>
                    <div class="font-bold">{{ depositDetailData.transactionNumber }}</div>

                    <div class="text-opacity-50">User ID:</div>
                    <div class="font-bold">{{ depositDetailData.payId }}</div>

                    <div class="text-opacity-50">Type/Status:</div>
                    <span class="flex">

                        {{ depositDetailData.typeMSG }} /
                        <div class="font-bold"><span :style="depositDetailData.status === 1 ? 'color: green' : 'color: red'"
                                style="border: 2px; padding: 2px;">
                                {{ depositDetailData.statusMSG }}
                            </span></div>

                    </span>
                    <div class="col-span-2">
                        <h3 class="font-bold text-xl mt-4 mb-2">From</h3>
                    </div>


                    <div class="text-opacity-50">Card Number:</div>
                    <div class="font-bold">{{ depositDetailData.cardNumber }}</div>

                    <div class="text-opacity-50">Expiry Date:</div>
                    <div class="font-bold">{{ depositDetailData.expiryDate }}</div>

                    <div class="text-opacity-50">CVV:</div>
                    <div class="font-bold">{{ depositDetailData.cvv }}</div>

                    <div class="text-opacity-50">Full Name:</div>
                    <div class="font-bold">{{ depositDetailData.fullName }}</div>

                    <div class="text-opacity-50">Phone Number:</div>
                    <div class="font-bold">{{ depositDetailData.phoneNumber }}</div>

                    <div class="text-opacity-50">Bank Name:</div>
                    <div class="font-bold">{{ depositDetailData.bankName }}</div>


                    <div class="text-opacity-50">Amount:</div>
                    <div class="font-bold">{{ depositDetailData.amount }}</div>

                    <div class="text-opacity-50">Real Amount:</div>
                    <div class="font-bold">{{ depositDetailData.realAmount }}</div>

                    <div class="text-opacity-50">Commission:</div>
                    <div class="font-bold">{{ depositDetailData.commission }}</div>


                    <hr class="my-4 col-span-2">


                    <div class="text-opacity-50">Create Time:</div>
                    <div class="font-bold">{{ depositDetailData.createTime }}</div>

                    <div class="text-opacity-50">Complete Time:</div>
                    <div class="font-bold">{{ depositDetailData.completeTime }}</div>
                </div>
            </div>
        </popup>
    </div>
</template>

<script lang="ts" setup>
import { depositDetail } from '@/api/deposit'
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'

const emit = defineEmits(['success', 'close'])

const popupRef = shallowRef<InstanceType<typeof Popup>>()
const depositDetailData = ref(null) // Detay veriyi saklamak için bir ref tanımlayın

const open = () => {
    popupRef.value?.open()
}



const getDetail = async (depositId: number) => {
    try {
        const data = await depositDetail({
            detailId: depositId
        })
        console.log(data)
        depositDetailData.value = data // Veriyi ref'e atayın
    } catch (error) {
        console.error('Detay verisi alınamadı:', error)
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
