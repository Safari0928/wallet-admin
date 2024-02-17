<template>
    <div class="detail-popup">
        <popup ref="popupRef" title="transfer" :async="true" width="550px" @close="handleClose">
            <div v-if="transferDetailData">


                <div class="grid grid-cols-2 gap-2">

                    <div class="text-opacity-50">Transaction Number:</div>
                    <div class="font-bold">{{ transferDetailData.transactionNumber }}</div>


                    <div class="text-opacity-50">Type/Status:</div>
                    <div class="flex">
                        {{ transferDetailData.typeMSG }} /
                        <div class="font-bold"><span
                                :style="transferDetailData.status === 1 ? 'color: green' : 'color: red'"
                                style="border: 2px; padding: 2px;">
                                {{ transferDetailData.statusMSG }}
                            </span></div>
                    </div>

                    <hr class="col-span-2">
                    <div class="col-span-2">
                        <h3 class="font-bold text-xl ">From</h3>
                    </div>
                    <div class="text-opacity-50">User ID:</div>
                    <div class="font-bold ">{{ transferDetailData.payId }}</div>

                    <div class="col-span-2">
                        <h3 class="font-bold text-xl ">To</h3>
                    </div>
                    <div class="text-opacity-50">User ID:</div>
                    <div class="font-bold">{{ transferDetailData.toPayId }}</div>
                    <hr class="col-span-2">

                    <div class="text-opacity-50">Currency:</div>
                    <div class="font-bold">{{ transferDetailData.currency }}</div>

                    <div class="text-opacity-50">Amount:</div>
                    <div class="font-bold">{{ transferDetailData.amount }}</div>

                    <div class="text-opacity-50">Commission:</div>
                    <div class="font-bold">{{ transferDetailData.commission }} ({{ transferDetailData.channelCommission }} +
                        {{ transferDetailData.payCommission }} )</div>

                    <div class="text-opacity-50">Real Amount:</div>
                    <div class="font-bold">{{ transferDetailData.realAmount }}</div>


                    <div class="col-span-2">
                        <hr class="my-4">
                    </div>

                    <div class="text-opacity-50">Create Time:</div>
                    <div class="font-bold">{{ transferDetailData.createTime }}</div>

                    <div class="text-opacity-50">Complete Time:</div>
                    <div class="font-bold">{{ transferDetailData.completeTime }}</div>
                </div>





            </div>
        </popup>
    </div>
</template>

<script lang="ts" setup>
import { transferDetail } from '@/api/transfer'
import Popup from '@/components/popup/index.vue'
import { ref, shallowRef } from 'vue'

const emit = defineEmits(['success', 'close'])

const popupRef = shallowRef<InstanceType<typeof Popup>>()
const transferDetailData = ref(null)

const open = () => {
    popupRef.value?.open()
}


const getDetail = async (transferId: number) => {
    try {
        const data = await transferDetail({
            detailId: transferId
        })
        console.log(data)
        transferDetailData.value = data // Veriyi ref'e atayın
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
