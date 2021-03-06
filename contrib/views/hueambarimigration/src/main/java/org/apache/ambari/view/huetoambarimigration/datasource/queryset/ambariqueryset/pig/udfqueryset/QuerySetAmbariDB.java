/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.view.huetoambarimigration.datasource.queryset.ambariqueryset.pig.udfqueryset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Pig Script prepared statement
 */
public abstract class QuerySetAmbariDB {

  public PreparedStatement getTableIdFromInstanceName(Connection connection, String instance) throws SQLException {
    PreparedStatement prSt = connection.prepareStatement(getTableIdSqlFromInstanceName());
    prSt.setString(1, instance);
    return prSt;
  }

  public PreparedStatement getSequenceNoFromAmbariSequence(Connection connection,int id) throws SQLException {
    PreparedStatement prSt = connection.prepareStatement(getSqlSequenceNoFromAmbariSequence(id));
    return prSt;
  }


  public PreparedStatement insertToPigUdf(Connection connection, int id, String maxcount1, String udfName, String owner,String path) throws SQLException {

    PreparedStatement prSt = connection.prepareStatement(getSqlinsertToPigUdf(id));
    prSt.setString(1, maxcount1);
    prSt.setString(2, udfName);
    prSt.setString(3, owner);
    prSt.setString(4, path);

    return prSt;
  }


  public PreparedStatement updateSequenceNoInAmbariSequence(Connection connection, int seqNo, int id) throws SQLException {

    PreparedStatement prSt = connection.prepareStatement(getSqlUpdateSequenceNo(id));

    prSt.setInt(1, seqNo);

    return prSt;
  }

  protected String getSqlMaxDSidFromTableId(int id) {
    return "select MAX(cast(ds_id as integer)) as max from ds_udf_" + id + ";";
  }

  protected String getTableIdSqlFromInstanceName() {
    return "select id from viewentity where class_name LIKE 'org.apache.ambari.view.pig.resources.udf.models.UDF' and view_instance_name=?;";
  }

  protected String getSqlinsertToPigUdf(int id) {
    return "INSERT INTO ds_udf_" + id + " values (?,?,?,?);";
  }


  protected String getSqlSequenceNoFromAmbariSequence(int id) {
    return "select sequence_value from ambari_sequences where sequence_name ='ds_udf_"+id+"_id_seq';";
  }

  protected String getSqlUpdateSequenceNo(int id) {
    return "update ambari_sequences set sequence_value=? where sequence_name='ds_udf_"+id+"_id_seq';";
  }
}
