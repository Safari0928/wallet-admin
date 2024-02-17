<template>
    <div class="workbench grid gap-3">
        <div class="grid grid-cols-2 gap-3 md:grid-cols-4">
            <el-card v-for="(item, index) in workbenchData.panel" :key="index" class="!border-none md:mr-4 w-full"
                shadow="never">
                <div
                    class="flex flex-col justify-center items-start hover:-translate-y-2 ease-in-out duration-300 cursor-pointer p-3">
                    <div class="font-light opacity-70 text-sm">{{ item.content }}</div>
                    <div class="text-6xl mt-2 font-semibold ">
                        {{ index === 2 || index === 3 ? item.value : '₺' +
                            formatNumberWithTwoDecimalPlaces(item.value) }}
                    </div>
                </div>
            </el-card>

        </div>

        <div class="grid grid-cols-4">
            <el-card class="md:col-span-3 col-span-4 !border-none md:mr-4 mb-4" shadow="never">
                <template #header>
                    <div class="grid grid-cols-3 justify-between w-full">
                        <div class="col-span-2">
                            <el-radio-group v-model="selectedGraphParam" class="grid grid-cols-6 w-full">
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="1"
                                    @change="() => getGraphData()">Revenue</el-radio-button>
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="2"
                                    @change="() => getGraphData()">Withdrawal</el-radio-button>
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="3"
                                    @change="() => getGraphData()">Deposit</el-radio-button>
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="4"
                                    @change="() => getGraphData()">Transfer</el-radio-button>
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="5"
                                    @change="() => getGraphData()">Exchanges</el-radio-button>
                                <el-radio-button class=" py-2 rounded drop-shadow-md" :label="6"
                                    @change="() => getGraphData()">Users</el-radio-button>
                            </el-radio-group>
                        </div>
                        <div class="col-span-1 flex flex-col gap-3">
                            <span class="flex">

                                <el-date-picker size="small" v-model="startDate" type="datetime" placeholder="Start Date"
                                    format="YYYY/MM/DD" @change="() => getGraphData()"
                                    :locale="{ lang: 'en' }"></el-date-picker>
                                -
                                <el-date-picker size="small" v-model="endDate" type="datetime" placeholder="End Date"
                                    format="YYYY/MM/DD" @change="() => getGraphData()"
                                    :locale="{ lang: 'en' }"></el-date-picker>
                            </span>
                            <el-radio-group v-model="selectedGraphTime" class="grid grid-cols-6 w-full">
                                <el-radio-button class="rounded drop-shadow-md" :label="1" @click="() => {
                                    changeStartDate(1)

                                }
                                    ">Today</el-radio-button>
                                <el-radio-button class="rounded drop-shadow-md" :label="2" @click="() => {
                                    changeStartDate(7)
                                }
                                    ">Tweek</el-radio-button>
                                <el-radio-button class="rounded drop-shadow-md" :label="3" @click="() => {
                                    changeStartDate(30)
                                }
                                    ">Tmonth</el-radio-button>
                                <el-radio-button class="rounded drop-shadow-md" :label="4" @click="() => {
                                    changeStartDate(365)
                                }
                                    ">Tyear</el-radio-button>
                            </el-radio-group>
                        </div>
                    </div>
                </template>
                <div>
                    <v-charts style="height: 250px" :option="workbenchData.visitorOptionLine" :autoresize="true" />
                </div>
            </el-card>


            <el-card class="col-span-4 md:col-span-1 !border-none mb-4" shadow="never">

                <div>
                    <el-radio-group v-model="selectedChartParam" class="flex justify-end w-full">
                        <el-radio-button class="rounded drop-shadow-md" :label="1"
                            @change="() => getChartData()">All</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="3"
                            @change="() => getChartData()">Verify</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="2"
                            @change="() => getChartData()">Unverifed</el-radio-button>

                    </el-radio-group>
                </div>
                <div>
                    <v-charts style="height: 250px" :option="workbenchData.visitorOptionPie" :autoresize="true" />
                </div>
            </el-card>
        </div>
        <div class="grid grid-cols-2 gap-3">
            <el-card class="!border-none col-span-2 md:col-span-1" shadow="never">
                <div class="mb-2">
                    <div class="font-semibold">
                        Deposit Top Rank
                    </div>
                    <el-radio-group v-model="selectedDepositRank" class="flex justify-end w-full">
                        <el-radio-button class="rounded drop-shadow-md" :label="1" @change="() => {
                            changeDepositRankStartDate(1)
                        }">Today</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="2" @change="() => {
                            changeDepositRankStartDate(7)

                        }">Tweek</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="3" @change="() => {
                            changeDepositRankStartDate(365)

                        }">Season</el-radio-button>

                    </el-radio-group>
                </div>
                <el-table v-loading="pagerDeposit.pager.loading" :data="pagerDeposit.pager.lists">

                    <el-table-column type="index" />
                    <el-table-column label="User ID" prop="payId" min-width="160" show-tooltip-when-overflow />
                    <el-table-column label="Country" prop="country" min-width="160" />
                    <el-table-column label="Amount" prop="amount" min-width="120" />
                    <el-table-column label="Action" width="120" fixed="right">
                        <template #default="{ row }">
                            <router-link v-perms="['user:detail', 'user:detail']" :to="{
                                path: getRoutePath('user:detail'),
                                query: {
                                    id: row.userId
                                }

                            }">
                                <el-button>

                                    detail
                                </el-button>
                            </router-link>
                        </template>
                    </el-table-column>

                </el-table>
                <div class="flex justify-end mt-4">
                    <pagination v-model="pagerDeposit.pager" @change="pagerDeposit.getLists" />
                </div>
            </el-card>
            <el-card class=" !border-none col-span-2 md:col-span-1" shadow="never">
                
                <div class="mb-2">
                    <div class="font-semibold">
                        Withdrawal Top Rank
                    </div>
                    <el-radio-group v-model="selectedWithDrawalRank" class="flex justify-end w-full">
                        <el-radio-button class="rounded drop-shadow-md" :label="1" @change="() => {
                            queryParamsDeposit.startDate = getDate(1)
                            pagerWithdrawal.getLists()
                        }">Today</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="2" @change="() => {
                            queryParamsDeposit.startDate = getDate(7)
                            pagerWithdrawal.getLists()
                        }">Tweek</el-radio-button>
                        <el-radio-button class="rounded drop-shadow-md" :label="3" @change="() => {
                            queryParamsDeposit.startDate = getDate(365)
                            pagerWithdrawal.getLists()
                        }">Season</el-radio-button>

                    </el-radio-group>
                </div>
                <el-table v-loading="pagerWithdrawal.pager.loading" :data="pagerWithdrawal.pager.lists">

                    <el-table-column label="User ID" prop="payId" min-width="160" show-tooltip-when-overflow />
                    <el-table-column label="Country" prop="country" min-width="160" />
                    <el-table-column label="Amount" prop="amount" min-width="120" />
                    <el-table-column label="Action" width="120" fixed="right">
                        <template #default="{ row }">
                            <router-link v-perms="['user:detail', 'user:detail']" :to="{
                            path: getRoutePath('user:detail'),
                            query: {
                                id: row.userId
                            }

                        }">
                            <el-button>

                                detail
                            </el-button>
                        </router-link>
                        </template>
                    </el-table-column>

                </el-table>
                <div class="flex justify-end mt-4">
                    <pagination v-model="pagerWithdrawal.pager" @change="pagerWithdrawal.getLists" />
                </div>
            </el-card>
        </div>
        <deposit-popup v-if="showDetail" ref="depositPopupRef" @success="pagerDeposit.getLists"
            @close="showDetail = false" />
        <withdrawal-popup v-if="showDetail" ref="withdrawalPopupRef" @success="pagerWithdrawal.getLists"
            @close="showDetail = false" />
    </div>
</template>

<script lang="ts" setup name="workbench">
import { getGraph, getChart, getPanel, getRank } from '@/api/app'
import vCharts from 'vue-echarts'
import { usePaging } from '@/hooks/usePaging'
import { formatNumberWithTwoDecimalPlaces } from '@/utils/filters';
import depositPopup from '@/views/deposit/detail.vue'
import withdrawalPopup from '@/views/withdrawal/detail.vue'
import { getRoutePath } from '@/router'



const depositPopupRef = shallowRef<InstanceType<typeof depositPopup>>();
const withdrawalPopupRef = shallowRef<InstanceType<typeof withdrawalPopup>>();


const showDetail = ref(false)

const startDate = ref(getDate(7));
const endDate = ref(getDate(0));
const selectedGraphParam = ref(1);
const selectedGraphTime = ref(2);
const selectedChartParam = ref(1);
const selectedDepositRank = ref(1);
const selectedWithDrawalRank = ref(1);


const handleDetail = async (detailId: any, typeDetail: any) => {
    console.log(detailId, typeDetail);

    showDetail.value = true;
    await nextTick();

    if (typeDetail === 1) {
        // Deposit
        depositPopupRef.value?.open();
        depositPopupRef.value?.getDetail(detailId);
    } else if (typeDetail === 2) {
        // Transfer
        withdrawalPopupRef.value?.open();
        withdrawalPopupRef.value?.getDetail(detailId);
    }
}




// date procceses
function getDate(day: number) {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - day);
    return yesterday.toISOString().split('T')[0];
}


function changeDateFormat(inputDate: any) {
    var date = new Date(inputDate);
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var day = String(date.getDate()).padStart(2, '0');
    return year + '-' + month + '-' + day;
}

const changeStartDate = (day: number) => {
    startDate.value = getDate(day)
    getGraphData()
}

const changeDepositRankStartDate = (day: number) => {

    queryParamsDeposit.startDate = getDate(day)
    pagerDeposit.resetPage()

}


const workbenchData: any = reactive({

    panel: [

    ],

    visitorOptionLine: {
        xAxis: {
            type: 'category',
            data: [0]
        },
        yAxis: {
            type: 'value'
        },
        legend: {
            data: ['data']
        },
        itemStyle: {
            // 点的颜色。
            color: 'red'
        },
        tooltip: {
            trigger: 'axis'
        },
        series: [
            {
                name: 'first section',
                data: [0],
                type: 'line',
                smooth: true
            },
            {
                name: 'second section',
                data: [0],
                type: 'line',
                smooth: true
            }
        ]
    },
    visitorOptionPie: {
        tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        legend: {
            orient: "vertical",
            left: "left",
            data: [],
        },
        series: [
            {
                name: "",
                type: "pie",
                radius: "55%",
                center: ["50%", "60%"],
                data: [
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: "rgba(0, 0, 0, 0.5)",
                    },
                },
            },
        ],
    },
})


//panel
const getPanelData = async () => {


    const res = await getPanel({
        startDate: startDate.value,
        endDate: endDate.value,
    });
    workbenchData.panel = res
}


// chart
const getChartData = async () => {

    const res = await getChart({
        type: selectedChartParam.value,
    });


    workbenchData.visitorOptionPie.legend.data = []
    workbenchData.visitorOptionPie.series[0].data = []

    /* workbenchData.visitorOptionPie.legend.data = res.params */
    workbenchData.visitorOptionPie.series[0].data = res.data
}


// Graph
const getGraphData = async () => {

    const thirdDate = Math.floor((new Date(endDate.value) - new Date(startDate.value)) / (1000 * 60 * 60 * 12))

    startDate.value = changeDateFormat(startDate.value)
    endDate.value = changeDateFormat(endDate.value)

    const res = await getGraph({
        startDate: startDate.value,
        endDate: endDate.value,
        type: selectedGraphParam.value
    });

    workbenchData.visitorOptionLine.xAxis.data = []
    workbenchData.visitorOptionLine.series[0].data = []

    workbenchData.visitorOptionLine.xAxis.data = res.date
    workbenchData.visitorOptionLine.series[0].data = res.data

    const res1 = await getGraph({
        startDate: getDate(thirdDate),
        endDate: startDate.value,
        type: selectedGraphParam.value
    });

    workbenchData.visitorOptionLine.series[1].data = res1.data

}


// Rank
const queryParamsDeposit = reactive({
    startDate: getDate(15),
    endDate: getDate(0),
    pageSize: 5
})

const queryParamsWithdrawal = reactive({
    startDate: getDate(15),
    endDate: getDate(0),
    pageSize: 5
})


const pagerDeposit = usePaging({
    fetchFun: getRank,
    params: { ...queryParamsDeposit, type: 1 }
})

const pagerWithdrawal = usePaging({
    fetchFun: getRank,
    params: { ...queryParamsWithdrawal, type: 2 }
})

const getData = async () => {
    await getPanelData()
    await getGraphData()
    await getChartData()
    await pagerDeposit.getLists()
    await pagerWithdrawal.getLists()
}

getData()
</script>

<style lang="scss" scoped></style>
