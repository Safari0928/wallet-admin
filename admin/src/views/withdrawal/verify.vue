<template>
    <div class="verify-popup">
        <popup ref="popupRef" title="verify" :async="true" width="550px" @close="handleClose" cancel-button-text="selam">
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
                        <div class="font-bold">
                            <span
                                :style="withdrawDetails.status === 1 ? 'color: green' : withdrawDetails.status === 2 ? 'color: orange' : 'color: red'"
                                style="border: 2px; padding: 2px;">
                                {{ withdrawDetails.statusMessage }}
                            </span>
                        </div>
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
                <span v-if="!showCancelInput">
                    <div class="flex justify-end my-2">
                        <el-button
                            class="btn btn-primary p-2 border-2 rounded-md mt-2 bg-blue-500 hover:bg-blue easy-in-out duration-300"
                            @click="approveWithdraw()">approve</el-button>

                        <el-button
                            class="btn btn-primary p-2 border-2 rounded-md mt-2 ml-2 bg-blue-500 hover:bg-blue easy-in-out duration-300"
                            @click="handleCancelClick">unapprove</el-button>
                    </div>
                </span>

                <div v-if="showCancelInput">
                    <div class="flex flex-col mt-2">
                        <textarea v-model="cancelReason" class="border p-2 rounded-md w-full"
                            placeholder="enter the reason of cancel" style="resize: vertical;"></textarea>
                        <el-button
                            class="btn btn-primary p-2 border-2 rounded-md bg-blue-500 hover:bg-blue easy-in-out duration-300 mt-2"
                            @click="cancelWithdraw">I approve the reason for
                            rejection</el-button>
                    </div>
                </div>
            </div>
        </popup>
    </div>
</template>


<script lang="ts" setup>
import { withdrawDetail, withdrawMakeApproved, withdrawCancel } from '@/api/withdraw'
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'
import feedback from '@/utils/feedback'


const popupRef = shallowRef<InstanceType<typeof Popup>>()
const withdrawDetails = ref(null)

const open = () => {
    popupRef.value?.open()
}


const cancelReason = ref('');
const showCancelInput = ref(false);

const cancelWithdraw = async () => {
    await feedback.confirm("are you sure?")

    try {
        if (withdrawDetails.value && cancelReason.value) {
            const result = await withdrawCancel({
                detailId: withdrawDetails.value.uuid,
                description: cancelReason.value
            });
            console.log('Withdraw cancelled:', result);
            popupRef.value?.close();
        }
    } catch (error) {
        console.error('Cancellation failed:', error);
    }
};

const handleCancelClick = () => {
    showCancelInput.value = true;
};



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

const approveWithdraw = async () => {
    await feedback.confirm("are you sure?")
    try {
        if (withdrawDetails.value) {
            const result = await withdrawMakeApproved({ detailId: withdrawDetails.value.uuid, processNo: 1 })
            console.log('Withdraw approved:', result)
            popupRef.value?.close()
        }
    } catch (error) {
        console.error('Approval failed:', error)
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