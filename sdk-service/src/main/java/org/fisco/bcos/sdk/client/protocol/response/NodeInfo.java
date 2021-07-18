package org.fisco.bcos.sdk.client.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import org.fisco.bcos.sdk.model.JsonRpcResponse;

public class NodeInfo extends JsonRpcResponse<NodeInfo.NodeInformation> {
    public NodeInfo.NodeInformation getNodeInfo() {
        return getResult();
    }

    public static class NodeInformation {
        @JsonProperty("NodeID")
        private String nodeID;

        @JsonProperty("IPAndPort")
        private String ipAndPort;

        @JsonProperty("Agency")
        private String agency;

        @JsonProperty("Node")
        private String node;

        @JsonProperty("Topic")
        private List<String> topic;

        @JsonProperty("FISCO-BCOS Version")
        private String version;

        @JsonProperty("Supported Version")
        private String supportedVersion;

        @JsonProperty("Chain Id")
        private String chainId;

        @JsonProperty("Build Time")
        private String buildTime;

        @JsonProperty("Build Type")
        private String buildType;

        @JsonProperty("Git Branch")
        private String gitBranch;

        @JsonProperty("Git Commit Hash")
        private String gitCommitHash;

        public String getNodeID() {
            return nodeID;
        }

        public void setNodeID(String nodeID) {
            this.nodeID = nodeID;
        }

        public String getIpAndPort() {
            return ipAndPort;
        }

        public void setIpAndPort(String ipAndPort) {
            this.ipAndPort = ipAndPort;
        }

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public List<String> getTopic() {
            return topic;
        }

        public void setTopic(List<String> topic) {
            this.topic = topic;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSupportedVersion() {
            return supportedVersion;
        }

        public void setSupportedVersion(String supportedVersion) {
            this.supportedVersion = supportedVersion;
        }

        public String getChainId() {
            return chainId;
        }

        public void setChainId(String chainId) {
            this.chainId = chainId;
        }

        public String getBuildTime() {
            return buildTime;
        }

        public void setBuildTime(String buildTime) {
            this.buildTime = buildTime;
        }

        public String getBuildType() {
            return buildType;
        }

        public void setBuildType(String buildType) {
            this.buildType = buildType;
        }

        public String getGitBranch() {
            return gitBranch;
        }

        public void setGitBranch(String gitBranch) {
            this.gitBranch = gitBranch;
        }

        public String getGitCommitHash() {
            return gitCommitHash;
        }

        public void setGitCommitHash(String gitCommitHash) {
            this.gitCommitHash = gitCommitHash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInformation that = (NodeInformation) o;
            return Objects.equals(nodeID, that.nodeID)
                    && Objects.equals(node, that.node)
                    && Objects.equals(agency, that.agency)
                    && Objects.equals(ipAndPort, that.ipAndPort)
                    && Objects.equals(topic, that.topic)
                    && Objects.equals(version, that.version)
                    && Objects.equals(supportedVersion, that.supportedVersion)
                    && Objects.equals(chainId, that.chainId)
                    && Objects.equals(buildTime, that.buildTime)
                    && Objects.equals(buildType, that.buildType)
                    && Objects.equals(gitBranch, that.gitBranch)
                    && Objects.equals(gitCommitHash, that.gitCommitHash);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    node,
                    nodeID,
                    agency,
                    ipAndPort,
                    topic,
                    version,
                    supportedVersion,
                    chainId,
                    buildTime,
                    buildType,
                    gitBranch,
                    gitCommitHash);
        }

        @Override
        public String toString() {
            return "NodeInfo{"
                    + "nodeId='"
                    + nodeID
                    + '\''
                    + ", iPAndPort='"
                    + ipAndPort
                    + '\''
                    + ", node='"
                    + node
                    + '\''
                    + ", agency='"
                    + agency
                    + '\''
                    + ", topic='"
                    + topic
                    + '\''
                    + ", version='"
                    + version
                    + '\''
                    + ", supportedVersion='"
                    + supportedVersion
                    + '\''
                    + ", chainId='"
                    + chainId
                    + '\''
                    + ", buildTime='"
                    + buildTime
                    + '\''
                    + ", buildType='"
                    + buildType
                    + '\''
                    + ", gitBranch='"
                    + gitBranch
                    + '\''
                    + ", gitCommitHash='"
                    + gitCommitHash
                    + '\''
                    + '}';
        }
    }
}
