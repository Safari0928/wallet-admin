<template>
    <div class="deposit-list">
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">

                <el-form-item>
                    <el-select v-model="selectedField">
                        <el-option label="Transaction Number" value="transactionNumber" />
                        <el-option label="User Id" value="payId" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input class="w-[280px]" v-model="queryParams[selectedField]" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">Search</el-button>
                    <el-button @click="resetParams">Reset</el-button>
                </el-form-item>

            </el-form>
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-form-item class="flex justify-self-end">
                <el-button type="primary" @click="exportSelected">Export Selected</el-button>
            </el-form-item>
            <el-table v-loading="pager.loading" :data="pager.lists" @selection-change="handleSelectionChange">

                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="Transaction Number" prop="transactionNumber" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            <el-tooltip class="box-item" effect="dark" :content="row.transactionNumber" placement="top">
                                {{ formatTransactionNumber(row.transactionNumber) }}
                            </el-tooltip>
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="currency" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="currrencyFilter" placeholder="Type" @change="handleFilterChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="TRY" value="1"></el-option>
                                    <el-option label="EUR" value="2"></el-option>
                                    <el-option label="USD" value="3"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </template>
                    <template #default="{ row }">
                        {{ row.currency }}
                    </template>
                </el-table-column>
                <el-table-column label="Amount" prop="amount" min-width="140" />
                <el-table-column label="Real Amount" prop="realAmount" min-width="140" />
                <el-table-column label="transaction type" min-width="200">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="typeFilter" placeholder="Type" @change="handleFilterChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="Deposit" value="1"></el-option>
                                    <el-option label="Withdrawal" value="2"></el-option>
                                    <el-option label="Transfer Deposit" value="3"></el-option>
                                    <el-option label="Transfer Withdrawal" value="4"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </template>
                    <template #default="{ row }">
                        {{ row.transactionType }}
                    </template>
                </el-table-column>
                <el-table-column label="Commision" prop="realAmount" min-width="120" />
                <el-table-column label="Channel Handling Fee" prop="channelHandlingFee" min-width="200" />
                <el-table-column label="Aipay Handling Fee" prop="aipayHandlingFee" min-width="200" />
                <!--  <el-table-column label="Action" width="120" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="handleDetail(row.depositId)">
                            detail
                        </el-button>
                    </template>
                </el-table-column> -->
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
        <detail-popup v-if="showDetail" ref="detailRef" @success="getLists" @close="showDetail = false" />

    </div>
</template>
<script lang="ts" setup name="deposit">
import { handlingFeeList } from '@/api/handlingfee'
import { usePaging } from '@/hooks/usePaging'
import detailPopup from "./detail.vue";
import { formatTransactionNumber, formatName } from '@/utils/filters';

const selectedRows = ref([]);

const handleSelectionChange = (rows: any[]) => {
    selectedRows.value = rows;
};

const exportSelected = () => {

    const selectedData = selectedRows.value.map(row => {
        return {
            transactionNumber: row.transactionNumber,
            cardNumber: row.cardNumber,
            bankName: row.bankName,
            payId: row.payId,
            fullName: row.fullName,
            amount: row.amount,
            realAmount: row.realAmount,
            statusMSG: row.statusMSG,
            createTime: row.createTime,
            completeTime: row.completeTime,
        };
    });


    import('xlsx').then(xlsx => {
        const ws = xlsx.utils.json_to_sheet(selectedData);
        const wb = xlsx.utils.book_new();
        xlsx.utils.book_append_sheet(wb, ws, 'Selected Data');
        xlsx.writeFile(wb, 'selected_data.xlsx');
    });
};

const selectedField = ref('transactionNumber');

const detailRef = shallowRef<InstanceType<typeof detailPopup>>()
const showDetail = ref(false)
const typeFilter = ref('');
const currrencyFilter = ref('');



const handleDetail = async (data: any) => {
    showDetail.value = true
    await nextTick()
    detailRef.value?.open()
    detailRef.value?.getDetail(data)
}


const queryParams = reactive({
    transactionNumber: '',
    payId: ''
})

const handleFilterChange = () => {
    queryParams.typeDetail = typeFilter.value;
    queryParams.currrencyType = currrencyFilter.value;
    resetPage();
};

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: handlingFeeList,
    params: queryParams
})




onActivated(() => {
    getLists()
})

getLists()
</script>
