KBRANCH ?= "v5.13/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.13/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.13/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.13/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.13/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.13/standard/base"
KBRANCH:qemuriscv32  ?= "v5.13/standard/base"
KBRANCH:qemux86  ?= "v5.13/standard/base"
KBRANCH:qemux86-64 ?= "v5.13/standard/base"
KBRANCH:qemumips64 ?= "v5.13/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "e60a114de1d12cc9cc522744442c0e212992b480"
SRCREV_machine:qemuarm64 ?= "0b6fb68102f301774228315b13aa9f5027f1bf3b"
SRCREV_machine:qemumips ?= "05f785b4ea88d196349c9fd07e09e867d9ce04cb"
SRCREV_machine:qemuppc ?= "ac41bcb4f779c128c43175f81c376a8b7a68e8d6"
SRCREV_machine:qemuriscv64 ?= "c1eb1eaf6fd3f1302b89194f629eafb9368a326a"
SRCREV_machine:qemuriscv32 ?= "c1eb1eaf6fd3f1302b89194f629eafb9368a326a"
SRCREV_machine:qemux86 ?= "c1eb1eaf6fd3f1302b89194f629eafb9368a326a"
SRCREV_machine:qemux86-64 ?= "c1eb1eaf6fd3f1302b89194f629eafb9368a326a"
SRCREV_machine:qemumips64 ?= "f775f677e4f8e1a57e39b85ac74f3052477cef6d"
SRCREV_machine ?= "c1eb1eaf6fd3f1302b89194f629eafb9368a326a"
SRCREV_meta ?= "ace78a314c1fcbf7bd34791f4b0d014c83bb9b2e"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE:class-devupstream = "-1"
SRCREV_machine:class-devupstream ?= "f17352f54186bbf084d147e15efb5340430aacae"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13.7"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
