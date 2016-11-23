# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu-16.10_amd64"
  config.vm.network "private_network", ip: "192.168.33.10"
  config.vm.synced_folder ".", "/vagrant"
  config.ssh.forward_agent = true
  config.vm.provider "virtualbox" do |provider|
    provider.name = "traning-cassandora"
    provider.gui = false
    provider.cpus = 1
    provider.memory = "1024"
  end
end
