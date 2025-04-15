package com.example.cloudsim;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancing {
    public static void main(String[] args) {
        CloudSim simulation = new CloudSim();

        Datacenter datacenter = createDatacenter(simulation);
        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);

        List<Vm> vmList = createVms();
        List<Cloudlet> cloudletList = createCloudlets();

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        simulation.start();

        System.out.println("\n========= CLOUDLET EXECUTION RESULTS =========");
        System.out.printf("%-12s%-12s%-12s%-15s%-15s%-12s%n", "CloudletID", "VM ID", "Status", "Start Time", "Finish Time", "Exec Time");
        for (Cloudlet cl : cloudletList) {
            System.out.printf("%-12d%-12d%-12s%-15.2f%-15.2f%-12.2f%n",
                    cl.getId(),
                    cl.getVm().getId(),
                    cl.getStatus(),
                    cl.getExecStartTime(),
                    cl.getFinishTime(),
                    cl.getActualCpuTime());
        }
    }

    private static Datacenter createDatacenter(CloudSim simulation) {
        List<Pe> peList = new ArrayList<>();
        peList.add(new PeSimple(1000));

        List<Host> hostList = new ArrayList<>();
        Host host = new HostSimple(2048, 10000, 1000000, peList);
        hostList.add(host);

        return new DatacenterSimple(simulation, hostList);
    }

    private static List<Vm> createVms() {
        List<Vm> vmList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Vm vm = new VmSimple(i, 1000, 1)
                    .setRam(512).setBw(1000).setSize(10000);
            vmList.add(vm);
        }
        return vmList;
    }

    private static List<Cloudlet> createCloudlets() {
        List<Cloudlet> cloudletList = new ArrayList<>();
        UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.5);
        for (int i = 0; i < 20; i++) {
            Cloudlet cloudlet = new CloudletSimple(10000, 1, utilizationModel);
            cloudlet.setId(i);
            cloudletList.add(cloudlet);
        }
        return cloudletList;
    }
}
