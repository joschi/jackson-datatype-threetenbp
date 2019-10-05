// Generated 28-Mar-2019 using Moditect maven plugin
module com.fasterxml.jackson.datatype.threetenbp {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports com.fasterxml.jackson.datatype.threetenbp;
    exports com.fasterxml.jackson.datatype.threetenbp.deser;
    exports com.fasterxml.jackson.datatype.threetenbp.deser.key;
    exports com.fasterxml.jackson.datatype.threetenbp.ser;
    exports com.fasterxml.jackson.datatype.threetenbp.ser.key;

    provides com.fasterxml.jackson.databind.Module with
        com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
}
